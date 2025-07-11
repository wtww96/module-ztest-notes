package com.dp.notes.ui.wx.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Cap.ROUND
import android.graphics.Paint.Join
import android.graphics.Paint.Style.FILL
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withTranslation
import com.dp.core.extension.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Random

/**
 * author Dq
 * date on 2025/7/9
 * description 语音录制的声音波纹动画View
 */
class VoiceWaveView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), CoroutineScope by MainScope() {

    // 线条数量,奇数
    private val lineCount = 25

    // 常量
    private val MIN_VOICE_SIZE = 4.dp
    private val MAX_VOICE_SIZE = 26.dp
    private val LINE_WIDTH = 2.dp // 音量条的宽度
    private val LINE_SPACE = 2.dp // 音量条之间的间距
    private val DURATION = 400 // 动画持续时间
    private val BG_ROUND_DP = 15 // 背景圆角大小，单位 dp

    // 背景颜色
    @ColorInt
    private var cancelBgColor: Int = "#F85050".toColorInt()

    @ColorInt
    private var normalBgColor: Int = "#EFEFEF".toColorInt()

    // 初始化 Paint
    private val linePaint = Paint().apply {
        color = Color.BLACK
        style = FILL
        strokeCap = ROUND
        strokeJoin = Join.ROUND
    }
    private val txtPaint = Paint().apply {
        color = Color.BLACK
        style = FILL
        strokeCap = ROUND
        strokeJoin = Join.ROUND
        textSize = 14f.dp
        isAntiAlias = true
    }
    private val bgPaint = Paint().apply {
        color = normalBgColor
        style = FILL
        strokeCap = ROUND
        strokeJoin = Join.ROUND
        isAntiAlias = true
    }

    // 动画和数据
    private val drawLines = ArrayList<DrawLine>()
    private var interpolator: Interpolator = BounceInterpolator()

    // 宽度相关
    private val initWidthRotas = 0.42f // 最开始显示的宽度占控件整个宽度的比例
    private val maxWidthRotas = 0.8f // 最大显示宽度
    private var widthRotas = initWidthRotas // 当前显示的宽度
    private var showWidth = 0 // 显示的宽度（像素）

    // 文本显示
    private var showText = false
    private var content: String? = null
    private val textRect = Rect()

    // 动画控制
    private var toBig = true // 是否正在变宽
    private var showVoiceSize = 40 // 当前音量大小（用于正常模式）


    // 检查模式下音量条的固定高度
    // private val checkModeItemSize = intArrayOf(4.dp, 5.dp, 6.dp, 7.dp, 6.dp, 5.dp, 4.dp)
    private val checkModeItemSize = floatArrayOf(4f.dp, 5.5f.dp, 7.5f.dp, 9f.dp, 7.5f.dp, 5.5f.dp, 4f.dp)

    private var defaultJob: Job? = null// 默认的音量跳动 动画
    private var checkJob: Job? = null// 自检循环波纹 动画
    private var widthJob: Job? = null// 宽度增加 动画

    private var isCheckMode = true // 是否处于检查模式,默认是,用于初始显示
    private var checkStarIndex = 0 // 自检模式的起始索引

    var ratios1 =
        floatArrayOf(0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.5f, 0.3f, 0.5f, 0.8f, 1.0f, 0.8f, 0.5f, 0.3f, 0.5f, 0.7f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f)

    var ratios2 =
        floatArrayOf(
            0.3f, 0.5f,
            0.7f,
            0.5f, 0.3f,
            0.6f,
            0.7f, 0.8f,
            0.7f,
            0.7f, 0.8f, 0.9f, 1f, 0.9f, 0.8f, 0.7f,
            0.7f,
            0.8f, 0.7f,
            0.6f,
            0.3f, 0.5f,
            0.7f,
            0.5f, 0.3f,
        )

    init {
        initView()
    }

    private fun initView() {
        createLines()
    }

    /**
     * 创建初始的音量条数据
     */
    private fun createLines() {
        drawLines.clear()

        repeat(lineCount) {
            // val maxSize = (MIN_VOICE_SIZE * ratio).toInt()
            val maxSize = MIN_VOICE_SIZE
            val rotas = ratios2[it] // Random().nextFloat()
            val rect = RectF(-LINE_WIDTH / 2f, -maxSize / 2f, LINE_WIDTH / 2f, maxSize / 2f)
            val drawLine = DrawLine(
                rectF = rect,
                maxSize = maxSize,
                lineSize = Random().nextInt(maxSize), // 初始随机高度
                rotas = rotas,
                // duration = (DURATION * (1.0f / rotas)).toInt() // 根据比例设置不同持续时间
                duration = (DURATION * rotas).toInt() // 根据比例设置不同持续时间
            )

            drawLines.add(drawLine)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 默认自检状态
        // checkLineLoopAnimation()
    }

    override fun onDraw(canvas: Canvas) {
        if (isInEditMode) return

        canvas.withTranslation(width / 2f, height / 2f) {
            // 绘制背景
            drawRoundRect(
                -showWidth / 2f, -height / 2f,
                showWidth / 2f, height / 2f,
                BG_ROUND_DP.toFloat().dp,
                BG_ROUND_DP.toFloat().dp,
                bgPaint
            )

            if (showText && content != null) {
                val txtHeight = textRect.height() / 2f
                val txtWidth = textRect.width() / 2f
                drawText(content!!, -txtWidth, txtHeight, txtPaint)
            } else {
                val offsetX = (drawLines.size - 1) * 1.0f / 2 * (LINE_WIDTH + LINE_SPACE)
                translate(-offsetX, 0f)
                drawLines.forEach { drawLine ->
                    drawRoundRect(drawLine.rectF, 5f, 5f, linePaint)
                    translate((LINE_WIDTH + LINE_SPACE).toFloat(), 0f)
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        showWidth = (widthRotas * w).toInt()
        // 只有在非文本模式下才启动动画
        if (!showText) {
            // lineHandler?.sendEmptyMessage(MessageWhat.ANIMATION.what)
        }
    }

    /**
     * 自检的循环波纹动画: 没有声音 或者 声音较小的情况下
     */
    private fun checkLineLoopAnimation() {
        checkJob?.cancel()
        checkJob = launch {
            // 自检模式,从右往左
            checkStarIndex = drawLines.lastIndex
            Log.e("hehe", ">>>>>>>>>>>>>>>>>  自检的循环波纹动画")
            // while (isActive && isCheckMode && !showText) {
            while (isActive && isCheckMode && checkStarIndex >= -checkModeItemSize.size) {
                // Log.e("hehe", "开始重绘 >>>>>>>>>>>>>> checkStarIndex=$checkStarIndex")
                for (i in drawLines.indices) {
                    val drawLine = drawLines[i]
                    val index = i - checkStarIndex
                    // Log.e("hehe", "i=$i   index=$index  checkStarIndex=$checkStarIndex")
                    if (index >= 0 && index < checkModeItemSize.size) {
                        drawLine.lineSize = checkModeItemSize[index].toInt()
                    } else {
                        drawLine.lineSize = MIN_VOICE_SIZE // 默认值
                    }
                    drawLine.rectF.top = -drawLine.lineSize / 2f
                    drawLine.rectF.bottom = drawLine.lineSize / 2f
                }
                checkStarIndex--
                if (checkStarIndex < -checkModeItemSize.size) {
                    checkStarIndex = drawLines.lastIndex
                }
                invalidate()
                delay(100)
            }
        }
    }

    /**
     * 默认的音量波纹跳动的动画
     */
    private fun defaultLineLoopAnimation() {
        defaultJob?.cancel()
        defaultJob = launch {
            // drawLines.forEachIndexed { index, drawLine ->
            //     val timeStep = 16.0f / drawLine.duration // 时间增长步长 (假设每帧16ms)
            //     var timeCompletion = drawLine.timeCompletion // 时间完成度
            //
            //     // Log.e("hehe", "drawLine.duration = ${drawLine.duration}")
            //     // Log.e("hehe", "timeStep = $timeStep")
            //     // Log.e("hehe", "timeCompletion = $timeCompletion")
            //
            //     timeCompletion += timeStep // 更新时间完成度
            //     val animationCompletion = interpolator.getInterpolation(timeCompletion) // 获取动画完成度
            //     // Log.e("hehe", "timeCompletion new = $timeCompletion")
            //     // Log.e("hehe", "animationCompletion = $animationCompletion")
            //
            //     if (index==0){
            //         Log.e(
            //             "hehe", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> duration=${drawLine.duration} " +
            //                     "timeStep=$timeStep   timeCompletion=$timeCompletion  animationCompletion=$animationCompletion"
            //         )
            //     }
            //
            //
            //
            //     var lineSize = 0
            //     // 更新音量条的高度
            //     if (drawLine.isShrinking) {
            //         // 变小
            //         lineSize = ((1 - animationCompletion) * drawLine.maxSize).toInt()
            //     } else {
            //         // 变大
            //         lineSize = (animationCompletion * drawLine.maxSize).toInt()
            //     }
            //
            //     // Log.e(
            //     //     "hehe", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>index=$index  duration=${drawLine.duration} " +
            //     //             "animationCompletion=$animationCompletion   maxSize=${drawLine.maxSize}  lineSize=$lineSize"
            //     // )
            //
            //     if (timeCompletion >= 1) {
            //         // 完成了单边的缩小或增长，则切换模式
            //         drawLine.isShrinking = !drawLine.isShrinking
            //         drawLine.timeCompletion = 0f
            //     } else {
            //         drawLine.timeCompletion = timeCompletion // 更新时间完成度
            //     }
            //
            //     // 对最小值进行过滤，防止过小或负数
            //     lineSize = lineSize.coerceIn(MIN_VOICE_SIZE, MAX_VOICE_SIZE)
            //     // Log.e("hehe", "lineSize = $lineSize")
            //     // Log.e("hehe", "--------------------------------------------------")
            //     //Log.e("hehe", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>index=$index lineSize=${lineSize}")
            //     drawLine.rectF.top = -lineSize * 1.0f / 2
            //     drawLine.rectF.bottom = lineSize * 1.0f / 2
            //     drawLine.lineSize = lineSize
            // }
            // invalidate()


            while (isActive && !showText && !isCheckMode) {
                drawLines.forEachIndexed { index, drawLine ->
                    val timeStep = 16.0f / drawLine.duration // 时间增长步长 (假设每帧16ms)
                    var timeCompletion = drawLine.timeCompletion // 时间完成度

                    // Log.e("hehe", "drawLine.duration = ${drawLine.duration}")
                    // Log.e("hehe", "timeStep = $timeStep")
                    // Log.e("hehe", "timeCompletion = $timeCompletion")

                    timeCompletion += timeStep // 更新时间完成度
                    val animationCompletion = interpolator.getInterpolation(timeCompletion) // 获取动画完成度
                    // Log.e("hehe", "timeCompletion new = $timeCompletion")
                    // Log.e("hehe", "animationCompletion = $animationCompletion")

                    var lineSize = 0
                    // 更新音量条的高度
                    if (drawLine.isShrinking) {
                        // 变小
                        lineSize = ((1 - animationCompletion) * drawLine.maxSize).toInt()
                    } else {
                        // 变大
                        lineSize = (animationCompletion * drawLine.maxSize).toInt()
                    }

                    if (timeCompletion >= 1) {
                        // 完成了单边的缩小或增长，则切换模式
                        drawLine.isShrinking = !drawLine.isShrinking
                        drawLine.timeCompletion = 0f
                    } else {
                        drawLine.timeCompletion = timeCompletion // 更新时间完成度
                    }

                    // 对最小值进行过滤，防止过小或负数
                    lineSize = lineSize.coerceIn(MIN_VOICE_SIZE, (MAX_VOICE_SIZE * drawLine.rotas).toInt())
                    // Log.e("hehe", "lineSize = $lineSize")
                    // Log.e("hehe", "--------------------------------------------------")

                    if (index == 0) {
                        Log.e("hehe", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>index=$index lineSize=${lineSize}  timeCompletion=$timeCompletion")
                    }
                    drawLine.rectF.top = -lineSize * 1.0f / 2
                    drawLine.rectF.bottom = lineSize * 1.0f / 2
                    drawLine.lineSize = lineSize
                }
                invalidate()
                delay(16) // 16ms更新一次
            }
        }
    }

    /**
     * 宽度逐渐变宽的动画
     */
    private fun widthIncreaseAnimation() {
        widthJob?.cancel()
        widthJob = launch {
            while (widthRotas < maxWidthRotas) {
                widthRotas += 0.005f
                showWidth = (width * widthRotas).toInt()
                invalidate()
                delay(50)
            }
        }
    }

    /**
     * 设置音量大小，驱动音量条变化
     * @param voiceSize 音量大小（像素）
     */
    fun addVoiceSize(voiceSize: Int) {
        // 如果处于文本显示模式，不处理音量变化
        if (showText) return

        if (voiceSize < 30) {
            // 声音太小,没有处于自检模式: 执行自检模式动画
            if (!isCheckMode) {
                isCheckMode = true
                checkLineLoopAnimation()
            }
            return
        } else {
            isCheckMode = false
            checkJob?.cancel()
        }

        Log.e("hehe", "handleChangeVoiceSizeMessage   voiceSize=$voiceSize")
        // 正常声音: 执行默认音量跳动动画
        drawLines.forEach { drawLine ->
            drawLine.timeCompletion = 0f
            drawLine.isShrinking = false // 开始时都变大
            // drawLine.maxSize = (drawLine.duration * voiceSize / DURATION)
            // drawLine.maxSize = (int) (drawLine.rotas * voiceSize);
            drawLine.maxSize = (voiceSize * drawLine.rotas).toInt()
            // Log.e("hehe", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> maxsize=${drawLine.maxSize} ,rotas=${drawLine.rotas}")
            // drawLine.rectF.top = -lineSize * 1.0f / 2
            // drawLine.rectF.bottom = lineSize * 1.0f / 2
            // drawLine.lineSize = lineSize
        }
        // invalidate()
        // 默认跳动动画
        defaultLineLoopAnimation()

        // 持续收集声音,View宽度开始变大
        if (toBig) {
            toBig = false
            widthIncreaseAnimation()
        }
    }

    /**
     * 设置是否为取消状态，改变背景颜色
     */
    fun setCancel(cancel: Boolean) {
        bgPaint.color = if (cancel) cancelBgColor else normalBgColor
        invalidate() // 立即重绘以显示颜色变化
    }

    /**
     * 设置显示文本内容
     */
    fun setContent(content: String?) {
        // this.showText = content != null
        // this.content = content
        // content?.let {
        //     txtPaint.getTextBounds(it, 0, it.length, textRect)
        // }
        // // 当文本内容改变时，应该停止动画并立即重绘
        // lineHandler?.removeMessages(MessageWhat.ANIMATION.what)
        // lineHandler?.removeMessages(MessageWhat.BIG.what)
        // lineHandler?.removeMessages(MessageWhat.CHECK_VOICE.what)
        // invalidate()
        //
        // handler.sendEmptyMessageDelayed(MessageWhat.ANIMATION.what, 16)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        defaultJob?.cancel()
        checkJob?.cancel()
        widthJob?.cancel()
    }

    // 静态内部类改为密封类，更安全地表示音量条状态
    private data class DrawLine(
        var rectF: RectF,
        var maxSize: Int,
        var lineSize: Int,
        var rotas: Float = 1.0f,
        var isShrinking: Boolean = true, // 是否缩小模式，原 small
        var timeCompletion: Float = 0f, // 时间完成度，返回在0-1
        var duration: Int
    )
}
