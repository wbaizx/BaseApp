package com.baseapp.main.lyrics

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.max

private const val ANIMATOR_DURATION = 300L

class ReadToolTextView(context: Context?) : View(context) {
    //数据源
    lateinit var textBeans: ArrayList<TextBean>

    //组装的数据(按当前尺寸计算)
    var lineDataList = arrayListOf<LineData>()

    //组装的数据(按最大值计算)
    private var maxLineDataList = arrayListOf<LineData>()

    //控件宽度
    private var viewWidth = 0

    //是否允许绘制拼音
    private var isDrawPinyin = true

    //字间距
    private var textSpacing: Float = 20f

    //行间距
    private var lineSpacing: Float = 30f

    private var maxTextSize = 60f
    private var minTextSize = 40f
    private var currentTextSize = minTextSize

    private var maxPinyinSize = 30f
    private var minPinyinSize = 20f
    private var currentPinyinSize = minPinyinSize

    private val selectColor = Color.parseColor("#FF3700B3")
    private val unSelectColor = Color.parseColor("#213456")

    private var valueAnimator: ValueAnimator? = null

    private val textPaint = Paint()
    private val pinyinPaint = Paint()

    init {
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = currentTextSize
        textPaint.color = unSelectColor

        pinyinPaint.textAlign = Paint.Align.CENTER
        pinyinPaint.textSize = currentPinyinSize
        pinyinPaint.color = unSelectColor
    }

    /**
     * 设置数据源
     */
    fun setData(textBeans: ArrayList<TextBean>) {
        this.textBeans = textBeans
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        viewWidth = if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            MeasureSpec.getSize(widthMeasureSpec)
        } else {
            0
        }

        //按最大高度计算一次尺寸，并给控件设置高度
        maxLineDataList = calculateData(textBeans, viewWidth.toFloat(), maxTextSize, maxPinyinSize)
        setMeasuredDimension(viewWidth, maxLineDataList.sumBy { it.lineHeight.toInt() })
    }

    /**
     * 所有排版数据计算
     */
    private fun calculateData(
        textBeans: ArrayList<TextBean>?,
        measuredWidth: Float,
        textSize: Float,
        pinyinSize: Float
    ): ArrayList<LineData> {
        if (textBeans.isNullOrEmpty()) return arrayListOf()

        textPaint.textSize = textSize
        pinyinPaint.textSize = pinyinSize

        var lineData = createLineData(isDrawPinyin)
        val list = arrayListOf(lineData)

        textBeans.forEachIndexed { index, textBean ->
            // 计算每个文本的宽度
            val textWidth = calculateTextWidth(textBean, lineData.isDrawPinyin)

            //超出一行了
            if (lineData.allTextWidth + textWidth > measuredWidth) {
                //新起一行
                lineData = createLineData(isDrawPinyin)
                list.add(lineData)
            }

            lineData.putText(LineData.TextData(textBean.word, textBean.pinyin, textWidth, index))
        }

        return list
    }

    private fun createLineData(isDrawPinyin: Boolean): LineData {
        val lineData = LineData()
        lineData.isDrawPinyin = isDrawPinyin
        lineData.lineSpacing = lineSpacing

        if (isDrawPinyin) {
            lineData.pinyinHeight = getTextHeight(pinyinPaint)
        }

        lineData.textHeight = getTextHeight(textPaint)
        return lineData
    }

    /**
     * 计算文本宽度，包含字左右间距
     */
    private fun calculateTextWidth(bean: TextBean, drawPinyin: Boolean): Float {
        val widthWord = textPaint.measureText(bean.word)
        val widthPinyin = if (drawPinyin && bean.pinyin != null) {
            pinyinPaint.measureText(bean.pinyin)
        } else {
            0f
        }
        //取最大宽度+字间距作为文本对象宽度
        return max(widthWord, widthPinyin) + textSpacing
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas == null) return
        lineDataList = calculateData(textBeans, viewWidth.toFloat(), currentTextSize, currentPinyinSize)

        var lineTopY = 0f
        lineDataList.forEach { lineList ->
            drawLine(canvas, lineTopY, lineList)
            lineTopY += lineList.lineHeight
        }
    }

    private fun drawLine(canvas: Canvas, lineTopY: Float, lineData: LineData) {
        var textStartX = 0f
        lineData.textList.forEach { textData ->
            val textCenterX = textStartX + textData.width / 2
            drawText(canvas, textData, lineData, textCenterX, lineTopY)
            textStartX += textData.width
        }
    }

    private fun drawText(canvas: Canvas, textData: LineData.TextData, lineData: LineData, textCenterX: Float, lineTopY: Float) {
        if (!textData.pinyin.isNullOrEmpty() && lineData.isDrawPinyin) {
            val topPinyinY = getTextOffset(lineTopY, pinyinPaint) + lineData.lineSpacing / 2
            canvas.drawText(textData.pinyin, textCenterX, topPinyinY, pinyinPaint)
        }

        val topTextY = if (lineData.isDrawPinyin && lineData.hasPinyin) {
            getTextOffset(lineTopY, textPaint) + lineData.pinyinHeight + lineData.lineSpacing / 2
        } else {
            getTextOffset(lineTopY, textPaint) + lineData.lineSpacing / 2
        }
        canvas.drawText(textData.word, textCenterX, topTextY, textPaint)
    }

    /**
     * 根据文字顶部对齐偏移量计算坐标
     */
    private fun getTextOffset(point: Float, paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return point + 0 - fontMetrics.top
    }

    /**
     * 得到文字高度
     */
    private fun getTextHeight(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return fontMetrics.bottom - fontMetrics.top
    }

    /**
     * view销毁时
     */
    override fun onDetachedFromWindow() {
        closeAnimator()
        super.onDetachedFromWindow()
    }

    private fun closeAnimator() {
        valueAnimator?.run {
            removeAllUpdateListeners()
            if (isRunning) cancel()
        }
    }

    /**
     * 通用动画逻辑
     */
    private fun initAnimator(update: (Float) -> Unit, delay: Long = 0) {
        valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = ANIMATOR_DURATION
            interpolator = LinearInterpolator()
            addUpdateListener {
                val currentValue = it.animatedValue as? Float
                currentValue?.let { value ->
                    update(value)
                    invalidate()
                }
            }
            startDelay = delay
            start()
        }
    }

    /**
     * 变大动画
     */
    fun changeSizeToMax() {
        closeAnimator()

        if (currentTextSize == maxTextSize && currentPinyinSize == maxPinyinSize) return

        pinyinPaint.color = selectColor
        textPaint.color = selectColor
        textPaint.typeface = Typeface.DEFAULT_BOLD
        pinyinPaint.typeface = Typeface.DEFAULT_BOLD

        val currentTextSizeForChange = currentTextSize
        val textOff = maxTextSize - currentTextSizeForChange

        val currentPinyinSizeForChange = currentPinyinSize
        val pinyinOff = maxPinyinSize - currentPinyinSizeForChange

        initAnimator({ value ->
            currentTextSize = currentTextSizeForChange + value * textOff
            currentPinyinSize = currentPinyinSizeForChange + value * pinyinOff
        }, 300)
    }

    /**
     * 变小动画
     */
    fun changeSizeToMin() {
        closeAnimator()

        if (currentTextSize == minTextSize && currentPinyinSize == minPinyinSize) return

        pinyinPaint.color = unSelectColor
        textPaint.color = unSelectColor
        textPaint.typeface = Typeface.DEFAULT
        pinyinPaint.typeface = Typeface.DEFAULT

        val currentTextSizeForChange = currentTextSize
        val textOff = currentTextSizeForChange - minTextSize

        val currentPinyinSizeForChange = currentPinyinSize
        val pinyinOff = currentPinyinSizeForChange - minPinyinSize

        initAnimator({ value ->
            currentTextSize = currentTextSizeForChange - value * textOff
            currentPinyinSize = currentPinyinSizeForChange - value * pinyinOff
        })
    }

    /**
     * 按最大值获取对应行中点所在高度
     */
    fun getLineCenterTopForMax(lineIndex: Int): Int {
        var top = 0f
        repeat(lineIndex) {
            top += maxLineDataList[it].lineHeight
        }
        top += maxLineDataList[lineIndex].lineHeight / 2

        return top.toInt()
    }
}