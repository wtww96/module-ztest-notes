package com.area.target.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior
import java.lang.reflect.Field


/**
 * author Dq
 * date on 2022/12/14
 * description 解决AppBarLayout过高 滑动抖动
 */
class FixAppBarLayoutBehavior(context: Context?, attrs: AttributeSet?) : Behavior(context, attrs) {
    private val TYPE_FLING = 1
    private var isFlinging = false
    private var shouldBlockNestedScroll = false

    private var flingRunnableField: Field? = null
    private var scrollerField: Field? = null

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: AppBarLayout, ev: MotionEvent): Boolean {
        shouldBlockNestedScroll = false
        if (isFlinging) {
            shouldBlockNestedScroll = true
        }

        when (ev.actionMasked) {
            //手指触摸屏幕的时候停止fling事件
            MotionEvent.ACTION_DOWN -> stopAppbarLayoutFling(child)
        }

        return super.onInterceptTouchEvent(parent, child, ev)
    }

    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        stopAppbarLayoutFling(child)
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: AppBarLayout, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (type == TYPE_FLING) {
            isFlinging = true
        }
        if (!shouldBlockNestedScroll) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        }
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        if (shouldBlockNestedScroll) return
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
    }

    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, abl: AppBarLayout, target: View, type: Int) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
        isFlinging = false
        shouldBlockNestedScroll = false
    }

    /**
     * 停止appbarLayout的fling事件
     */
    private fun stopAppbarLayoutFling(appBarLayout: AppBarLayout) {
        try {
            flingRunnableField = flingRunnableField ?: getFlingRunnableField()
            scrollerField = flingRunnableField ?: getScrollerField()
            flingRunnableField?.isAccessible = true
            scrollerField?.isAccessible = true

            val flingRunnable = flingRunnableField?.get(this) as? Runnable
            val overScroller = scrollerField?.get(this) as? OverScroller

            if (null != flingRunnable && null != flingRunnableField) {
                appBarLayout.removeCallbacks(flingRunnable)
                flingRunnableField!![this] = null
            }
            if (overScroller != null && !overScroller.isFinished) {
                overScroller.abortAnimation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 反射获取私有的flingRunnable 属性
     */
    private fun getFlingRunnableField(): Field? {
        return try {
            //android 28及以上版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass.superclass
            headerBehaviorType.getDeclaredField("flingRunnable")
        } catch (e: NoSuchFieldException) {
            //android 27及以下版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass
            headerBehaviorType.getDeclaredField("mFlingRunnable")
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 反射获取私有的scroller 属性
     */
    private fun getScrollerField(): Field? {
        return try {
            //android 28及以上版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass.superclass
            headerBehaviorType.getDeclaredField("scroller")
        } catch (e: NoSuchFieldException) {
            //android 27及以下版本
            val headerBehaviorType: Class<*> = this.javaClass.superclass.superclass
            headerBehaviorType.getDeclaredField("mScroller")
        } catch (e: Exception) {
            null
        }
    }
}