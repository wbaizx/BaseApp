package com.baseapp.main.coordinator.coordinator1

import android.graphics.*
import com.base.common.getBaseActOrAppContext
import com.base.common.util.dp2px
import com.base.common.util.sp2px
import com.base.common.view.OVER_TOP_ANIMATE_PUSH
import com.base.common.view.SimpleItemDecoration
import com.baseapp.R
import com.chad.library.adapter.base.BaseQuickAdapter

class Coordinator1Decoration(private val adapter: BaseQuickAdapter<String, *>) : SimpleItemDecoration() {

    override var decorationHeight: Int = dp2px(90f).toInt()

    //列表头部偏移量
    private val positionOffset = 1

    private val bgColor: Int = Color.parseColor("#898989")
    private val bgColor2: Int = Color.parseColor("#FF9500")

    /**
     * 画笔
     */
    private val mPaint: Paint = Paint()

    private val textTitleColor = Color.BLACK
    private val textTitleSize = sp2px(18f)
    private val textTitleLineColor = Color.parseColor("#BBEDFF")

    private val textDayColor = Color.parseColor("#9B9B9B")
    private val textDaySize = sp2px(16f)

    private val textTimeColor = Color.parseColor("#B4B4B4")
    private val textTimeSize = sp2px(12f)

    private val textLabelColor = Color.parseColor("#5CC1FF")
    private val icon =
        BitmapFactory.decodeResource(getBaseActOrAppContext().resources, R.mipmap.bill_details_main_item_line_icon)

    //粗体文字
    private val BOLD = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

    //普通文字
    private val DEFAULT = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

    //图片位置矩形
    private val rectF = RectF(0f, 0f, 0f, 0f)

    //图标左偏移
    private val iconLeftOff = dp2px(34f)

    //图标右偏移
    private val iconRightOff = dp2px(16f)

    //画笔宽度
    private val strokeWidth1 = dp2px(3f)
    private val strokeWidth2 = dp2px(6f)


    init {
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.textAlign = Paint.Align.LEFT
    }

    override fun offsets(outRect: Rect, childPosition: Int) {
        if (childPosition > -1 + positionOffset)
            outRect.set(0, decorationHeight, 0, 0)
    }

    override fun drawDecoration(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, childPosition: Int) {
        if (childPosition > -1 + positionOffset) {
            val dataPosition = childPosition - positionOffset
            drawTitleArea(c, left, right, top, bottom, dataPosition, adapter.getItem(dataPosition) ?: "", bgColor)
        }
    }

    override fun overTopAnimate(firstVisiblePosition: Int) = OVER_TOP_ANIMATE_PUSH

    override fun drawOverTop(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, firstVisiblePosition: Int) {
        if (firstVisiblePosition > -1 + positionOffset) {
            val dataPosition = firstVisiblePosition - positionOffset
            drawTitleArea(c, left, right, top, bottom, dataPosition, adapter.getItem(dataPosition) ?: "", bgColor2)
        }
    }

    override fun drawOverDecoration(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, childPosition: Int) {
    }

    /**
     * 绘制方法
     */
    private fun drawTitleArea(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, position: Int, data: String, bgColor: Int) {
        //画title背景矩形
        mPaint.color = bgColor
        c.drawRect(left, top, right, bottom, mPaint)

        //画内容

        //大title文字高度一半

        mPaint.typeface = BOLD
        mPaint.textSize = textTitleSize
        val height = (mPaint.fontMetrics.descent - mPaint.fontMetrics.ascent) / 2

        //画竖线
        mPaint.strokeWidth = strokeWidth1
        mPaint.color = textLabelColor
        var startX = dp2px(17f)
        var bottomY = top + decorationHeight / 2 + height
        c.drawLine(startX, bottomY - strokeWidth1 / 2, startX, bottomY - dp2px(13f) + strokeWidth1 / 2, mPaint)

        //画天
        mPaint.color = textDayColor
        mPaint.textSize = textDaySize
        startX = dp2px(24f)
        c.drawText("DAY$position", startX, bottomY - dp2px(7f) - getTextOffset(mPaint), mPaint)

        //画大title--------------------------
        startX += dp2px(5f) + mPaint.measureText("DAY$position")
        //画大title下划线
        mPaint.textSize = textTitleSize

        val titleWidth = mPaint.measureText(data)
        mPaint.strokeWidth = strokeWidth2
        mPaint.color = textTitleLineColor
        c.drawLine(
            startX + strokeWidth2 / 2,
            bottomY - strokeWidth2 / 2,
            startX + titleWidth - strokeWidth2 / 2,
            bottomY - strokeWidth2 / 2,
            mPaint
        )

        //画大title字
        mPaint.color = textTitleColor
        c.drawText(data, startX, top + decorationHeight / 2 - getTextOffset(mPaint), mPaint)


        //画日期
        mPaint.typeface = DEFAULT
        startX = dp2px(24f)
        bottomY += dp2px(9f)
        mPaint.color = textTimeColor
        mPaint.textSize = textTimeSize

        c.drawText("--$position", startX, bottomY + dp2px(6f) - getTextOffset(mPaint), mPaint)

        //画图标
        rectF.set(
            right - iconLeftOff,
            top + decorationHeight / 2 + height - icon.height,
            right - iconRightOff,
            top + decorationHeight / 2 + height
        )
        c.drawBitmap(icon, null, rectF, mPaint)
    }
}
