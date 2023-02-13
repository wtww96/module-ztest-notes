package com.dp.notes.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2

/**
 * author Dq
 * date on 2023/2/13
 * description 垂直ViewPager2滑动冲突
 * 用法:  <com.dp.notes.widget.ViewPager2Container
 *            android:layout_width="match_parent"
 *            android:layout_height="match_parent">
 *
 *            <androidx.viewpager2.widget.ViewPager2
 *                android:id="@+id/viewPager"
 *                android:layout_width="match_parent"
 *                android:layout_height="match_parent" />
 *
 *        </com.area.target.widget.ViewPager2Container>
 */
class ViewPager2Container(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var viewPager2: ViewPager2? = null
    private var startX = 0
    private var startY = 0

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView is ViewPager2) {
                viewPager2 = childView
                break
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (null == viewPager2) return super.onInterceptTouchEvent(ev)
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                viewPager2?.isUserInputEnabled = true
            }

            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val disX = Math.abs(endX - startX)
                val disY = Math.abs(endY - startY)
                viewPager2?.isUserInputEnabled = disY < disX
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> viewPager2?.isUserInputEnabled = true
        }
        return super.onInterceptTouchEvent(ev)
    }
}