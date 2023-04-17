package com.baseapp.main.special_rc.scrollto_rc

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.base.common.util.dp2px
import com.base.common.util.sp2px
import com.base.common.view.OVER_TOP_ANIMATE_PUSH
import com.base.common.view.SimpleItemDecoration

class ScrollToRCDecoration : SimpleItemDecoration() {

    override var decorationHeight: Int = dp2px(90f).toInt()

    private val bgColor: Int = Color.parseColor("#898989")
    private val bgColor2: Int = Color.parseColor("#FF9500")

    private val textColor: Int = Color.BLACK

    /**
     * 画笔
     */
    private val mPaint: Paint = Paint()

    init {
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.textAlign = Paint.Align.LEFT
        mPaint.textSize = sp2px(18f)
    }

    override fun offsets(outRect: Rect, childPosition: Int) {
        outRect.set(0, decorationHeight, 0, 0)
    }

    override fun drawDecoration(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, childPosition: Int) {
        drawTitleArea(c, left, right, top, bottom, childPosition, bgColor)
    }

    override fun overTopAnimate(firstVisiblePosition: Int) = OVER_TOP_ANIMATE_PUSH

    override fun drawOverTop(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, firstVisiblePosition: Int) {
        drawTitleArea(c, left, right, top, bottom, firstVisiblePosition, bgColor2)
    }

    override fun drawOverDecoration(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, childPosition: Int) {
    }

    /**
     * 绘制方法
     */
    private fun drawTitleArea(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, position: Int, bgColor: Int) {
        //画title背景矩形
        mPaint.color = bgColor
        c.drawRect(left, top, right, bottom, mPaint)

        mPaint.color = textColor
        //画文本
        c.drawText("no $position", left + 100, top + decorationHeight / 2 - getTextOffset(mPaint), mPaint)
    }
}
