package com.dp.notes.ui.wx.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.Random
import kotlin.math.abs

/**
 * author Dq
 * date on 2025/7/9
 * description
 */
class WeChatVoiceWaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barCount = 20
    private val barWidth = 2f.dp
    private val barSpacing = 2f.dp
    private val maxBarHeight = 24f.dp

    private val barHeights = FloatArray(barCount) { 0f }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 5000L
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            updateBarHeights()
            invalidate()
        }
    }
    private val random = Random()

    init {
        start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f

        val totalWidth = barCount * barWidth + (barCount - 1) * barSpacing
        val startX = centerX - totalWidth / 2

        for (i in 0 until barCount) {
            val x = startX + i * (barWidth + barSpacing)
            val h = barHeights[i]
            canvas.drawRoundRect(
                x, centerY - h / 2,
                x + barWidth, centerY + h / 2,
                barWidth / 2, barWidth / 2, paint
            )
        }
    }

    private fun updateBarHeights() {
        // 模拟跳动
        for (i in 0 until barCount) {
            val base = if (i == barCount / 2) 1f else 0.3f + 0.7f * (1 - abs(i - barCount / 2f) / (barCount / 2f))
            barHeights[i] = base * (10f + random.nextFloat() * (maxBarHeight - 10f))
        }
    }

    fun start() {
        if (!animator.isRunning) animator.start()
    }

    fun stop() {
        animator.cancel()
    }

    fun release() {
        animator.cancel()
    }

    // 扩展单位 dp -> px
    private val Float.dp: Float
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics
        )
}