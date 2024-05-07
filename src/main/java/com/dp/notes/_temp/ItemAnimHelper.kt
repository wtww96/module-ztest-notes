package com.dp.notes._temp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import com.dp.core.extension.scWidth
import com.dp.core.extension.screenSize
import java.lang.ref.WeakReference
import java.util.*
import kotlin.LazyThreadSafetyMode.NONE


/**
 * author Dq
 * date on 2023/11/21
 * description 直播列表item动画
 */
class ItemAnimHelper constructor(activity: Activity) {
    private var host: WeakReference<Activity>? = null
    private lateinit var startView: View
    private lateinit var endView: View

    private val maxTime = 1200L
    private var animFlag = 0

    //动画容器
    private var animContainer: ViewGroup? = null
    private var startAction: (() -> Unit)? = null
    private var endAction: ((animFlag: Int) -> Unit)? = null

    //终点View的气泡
    private var bubblePopup: PopupWindow? = null

    //屏幕对角线:用于计算动画时长
    private val screenDiagonal by lazy(NONE) {
        Math.sqrt(Math.pow(scWidth.toDouble(), 2.0) + Math.pow(screenSize.y.toDouble(), 2.0))
    }

    init {
        host = WeakReference(activity)
    }

    fun withStartAction(block: () -> Unit) = apply {
        startAction = block
    }

    fun withEndAction(block: (animFlag: Int) -> Unit) = apply {
        endAction = block
    }

    fun bindViews(start: View, end: View) = apply {
        startView = start
        endView = end
    }

    fun start() {
        if (!startView.isShown || !endView.isShown) return
        val startLocation = IntArray(2)
        val endLocation = IntArray(2)
        startView.getLocationOnScreen(startLocation)
        endView.getLocationOnScreen(endLocation)
        if (null == host?.get()) return
        handleAnim(createViewBitmapContainer(startLocation), startLocation, endLocation)
    }

    /**
     * 创建目标View的bitmap容器View
     */
    private fun createViewBitmapContainer(startLocation: IntArray): View {
        val bitmap = Bitmap.createBitmap(startView.width, startView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        startView.draw(canvas)
        //---------------------------
        return ImageView(host?.get()).apply {
            layoutParams = MarginLayoutParams(startView.width, startView.height).apply {
                topMargin = startLocation[1]
                //适配RTL
                if (isRtl()) {
                    marginStart = scWidth - startLocation[0] - startView.width
                } else {
                    marginStart = startLocation[0]
                }
            }
            pivotX = 0f
            pivotY = 0f
            setImageBitmap(bitmap)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleAnim(v: View, startLocation: IntArray, endLocation: IntArray) {
        host?.get()?.let { activity ->
            animContainer = animContainer ?: FrameLayout(activity).apply {
                layoutParams = FrameLayout.LayoutParams(-1, -1)
                background = null
                (activity.window.decorView as ViewGroup).addView(this)
            }
            //动画View添加到动画层
            animContainer?.addView(v)
        } ?: return

        //位移的水平距离
        val endX = endLocation[0] - startLocation[0]
        //位移的垂直距离
        val endY = endLocation[1] - startLocation[1]

        //起点View和终点View的直线距离(x,y坐标)
        val diagonal = Math.abs(
            Math.sqrt(Math.pow(startLocation[0] - endLocation[0].toDouble(), 2.0) + Math.pow(startLocation[1] - endLocation[1].toDouble(), 2.0))
        )
        //不同的平移距离,动画执行的时间不同
        val animTime = Math.max(maxTime * (diagonal / screenDiagonal), 400.0).toLong()
        //开始平移缩放动画
        v.animate()
            .translationX(endX.toFloat())
            .translationY(endY.toFloat())
            .scaleX(0.1f)
            .scaleY(0.1f)
            .setDuration(animTime)
            .setInterpolator(LinearInterpolator())
            .withStartAction {
                //禁止触摸下层View
                animContainer?.setOnTouchListener { v, event -> true }
                animFlag++
                startAction?.invoke()
            }
            .withEndAction {
                //恢复触摸下层View
                animContainer?.setOnTouchListener(null)
                animContainer?.removeView(v)
                animFlag--
                endAction?.invoke(animFlag)
                //显示气泡
                host?.get()?.let { showBubble(it, endLocation[1]) }
            }
            .start()
    }

    private fun showBubble(context: Context, y: Int) {
        /*if (!endView.isShown) return
        if (null == bubblePopup) {
            val binding = PopupHintBubbleBinding.inflate(LayoutInflater.from(context), null, false)
            bubblePopup = PopupWindow(binding.root, -2, -2, true)
            binding.tvHint.text = TranslateResource.getStringResources("homepage_askcard_tip")
        }
        //适配RTL
        if (isRtl()) {
            bubblePopup?.showAtLocation(endView, Gravity.TOP or Gravity.START, 0, y + endView.height + 10)
        } else {
            bubblePopup?.showAtLocation(endView, Gravity.TOP or Gravity.END, 0, y + endView.height + 10)
        }*/
    }

    private fun isRtl(): Boolean =
        TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL
}