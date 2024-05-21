package com.dp.notes._temp.multi

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State
import com.dp.core.extension.dp
import com.dp.core.extension.scWidth
import com.dp.notes._temp.multi.MultiItemBean.Companion.ItemBottomBtn
import com.dp.notes._temp.multi.MultiItemBean.Companion.ItemMiddleBtn
import com.dp.notes._temp.multi.MultiItemBean.Companion.ItemTopBtn

/**
 * author Dq
 * date on 2023/1/6
 * description item分割线
 */
class ItemDivider : RecyclerView.ItemDecoration() {
    private val verticalOffsets = 10.dp

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        when (val currentLineItemType = parent.adapter?.getItemViewType(position)) {
            ItemTopBtn -> {
                outRect.left = (5 * (spanCount - 1 - spanIndex)).dp
                outRect.right = (5 * spanIndex).dp
            }
            ItemMiddleBtn, ItemBottomBtn -> {
                val childCount = parent.adapter?.itemCount ?: 0
                val lastLinePosition = position - (spanIndex + 1)
                val nextLinePosition = position + (spanCount - spanIndex)
                val lastLineItemType = if (lastLinePosition in 0 until childCount) parent.adapter?.getItemViewType(lastLinePosition) else -999
                val nextLineItemType = if (nextLinePosition in 0 until childCount) parent.adapter?.getItemViewType(nextLinePosition) else -999
                val hasLastLine = lastLineItemType == currentLineItemType
                val hasNextLine = nextLineItemType == currentLineItemType
                outRect.top = if (!hasLastLine) verticalOffsets else 0
                outRect.bottom = if (!hasNextLine) verticalOffsets else 0
                outRect.left = (26 - 12 * spanIndex).dp
                outRect.right = (26 - 12 * (spanCount - spanIndex - 1)).dp
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {
        //绘制ItemViewType为 ItemMiddleBtn 的背景
        drawBackground(c, parent, ItemMiddleBtn)
        //绘制ItemViewType为 ItemBottomBtn 的背景
        drawBackground(c, parent, ItemBottomBtn)
    }

    private fun drawBackground(c: Canvas, parent: RecyclerView, type: Int) {
        val itemTypes = mutableListOf<Int>()
        for (i in 0 until parent.childCount) {
            val position = parent.getChildAdapterPosition(parent.getChildAt(i))
            itemTypes.add(parent.adapter?.getItemViewType(position) ?: -999)
        }
        val firstView = parent.getChildAt(itemTypes.indexOfFirst { it == type })
        val lastView = parent.getChildAt(itemTypes.indexOfLast { it == type })
        if (firstView != null && lastView != null) {
            val drawable = GradientDrawable()
            drawable.cornerRadius = 20f
            drawable.setColor(Color.parseColor("#ffffff"))
            drawable.setBounds(16.dp, firstView.top - verticalOffsets, scWidth - 16.dp, lastView.bottom + verticalOffsets)
            drawable.draw(c)
        }
    }
}