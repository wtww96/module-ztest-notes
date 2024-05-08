package com.dp.notes.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * author Dq
 * date on 2022/12/15
 * description 滑动缩放顶部view
 */
class ZoomConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private var zoomView: ConstraintLayout? = null

    //private var banner: ConvenientBanner<*>? = null
    private var isZooming = false
    private val mScrollRate = 0.4f //缩放系数:越大,变化的越大
    private val mReplyRate = 0.6f //回调系数:越大,回调越慢

    private var zoomViewHeight = 0 //原始高
    private var downY = 0f
    private var downX = 0f

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        /*if (null == zoomView || null == banner) {
            zoomView = findViewById(R.id.zoomLayout)
            banner = findViewById(R.id.bannger)
        }*/
    }

    override fun onFinishInflate() {
        /*if (null == zoomView || null == banner) {
           zoomView = findViewById(R.id.zoomLayout)
           banner = findViewById(R.id.bannger)
       }*/
        super.onFinishInflate()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        zoomView?.let { v ->
            //记录View y坐标
            val location = IntArray(2)
            v.getLocationOnScreen(location)
            val y = location[1]

            //记录原始高度
            if (zoomViewHeight <= 0) zoomViewHeight = v.measuredHeight
            when (ev?.action) {
                MotionEvent.ACTION_DOWN -> {
                    isZooming = false
                    downY = ev.y
                    downX = ev.x
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isZooming = false
                    recoverZoom()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (y == 0 || (y != 0 && isZooming)) {
                        if ((ev.y - downY > 0 && abs(ev.y - downY) > abs(ev.x - downX)) || isZooming) {
                            isZooming = true
                            var distance = max(((ev.y - downY) * mScrollRate).toInt(), 0)
                            distance = min(distance, 400)
                            startZoom(distance.toFloat())
                            return false
                        }
                    } else {
                        downY = ev.y
                    }
                }
                else -> {}
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 缩放图片
     */
    fun startZoom(zoom: Float) {
        if (null == zoomView || zoomViewHeight <= 0) return
        val scale = (zoomViewHeight + zoom) / zoomViewHeight
        val zoomHeight = (zoomViewHeight * scale).toInt()
        /*banner?.viewPager?.scaleX = scale
        banner?.viewPager?.scaleY = scale*/
        zoomView?.updateLayoutParams {
            height = zoomHeight
        }
    }

    /**
     * 松开手机 回弹动画
     */
    private fun recoverZoom() {
        if (zoomView?.measuredHeight == zoomViewHeight) return
        zoomView?.let { v ->
            val distance = (v.measuredHeight - zoomViewHeight).toFloat()
            val valueAnimator = ValueAnimator.ofFloat(distance, 0f).setDuration((distance * mReplyRate).toLong())
            valueAnimator.addUpdateListener { animation -> startZoom((animation.animatedValue as Float)) }
            valueAnimator.start()
        }
    }
}