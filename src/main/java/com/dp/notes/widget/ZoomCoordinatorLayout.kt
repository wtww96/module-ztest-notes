package com.area.target.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams

/**
 * author Dq
 * date on 2022/12/14
 * description CoordinatorLayout 缩放View
 */
class ZoomCoordinatorLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CoordinatorLayout(context, attrs) {
    private var zoomView: ConstraintLayout? = null
    private var banner: View? = null

    private val mScrollRate = 0.5f //缩放系数:越大,变化的越大
    private val mReplyRate = 0.5f //回调系数:越大,回调越慢

    private var zoomViewHeight = 0 //原始高
    private var downY = 0f
    private var downX = 0f

    override fun onLayoutChild(child: View, layoutDirection: Int) {
        super.onLayoutChild(child, layoutDirection)
        if (null == zoomView) {
            //zoomView = child.findViewById(R.id.zoomLayout)
            //banner = child.findViewById(R.id.bannger)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        zoomView?.let { v ->
            //记录原始高度
            if (zoomViewHeight <= 0) zoomViewHeight = v.measuredHeight
            //记录View y坐标
            val location = IntArray(2)
            v.getLocationOnScreen(location)
            val y = location[1]
            when (ev?.action) {
                MotionEvent.ACTION_DOWN -> {
                    downY = ev.y
                    downX = ev.x
                }
                MotionEvent.ACTION_UP -> {
                    //手指离开后恢复图片
                    recoverZoom()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (y == 0) {
                        if (Math.abs(ev.y - downY) > Math.abs(ev.x - downX)) {
                            val distance = Math.max(((ev.y - downY) * mScrollRate).toInt(), 0)
                            startZoom(distance.toFloat())
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

    /***
     * 缩放图片
     */
    fun startZoom(zoom: Float) {
        if (null == zoomView || zoomViewHeight <= 0 || zoom > 400) return
        val scale = (zoomViewHeight + zoom) / zoomViewHeight
        val zoomHeight = (zoomViewHeight * scale).toInt()
        //banner?.viewPager?.scaleX = scale
        //banner?.viewPager?.scaleY = scale
        zoomView?.updateLayoutParams {
            height = zoomHeight
        }
    }

    /***
     * 松开手机 回弹动画
     */
    private fun recoverZoom() {
        zoomView?.let { v ->
            val distance = (v.measuredHeight - zoomViewHeight).toFloat()
            val valueAnimator = ValueAnimator.ofFloat(distance, 0f).setDuration((distance * mReplyRate).toLong())
            valueAnimator.addUpdateListener { animation -> startZoom((animation.animatedValue as Float)) }
            valueAnimator.start()
        }
    }
}