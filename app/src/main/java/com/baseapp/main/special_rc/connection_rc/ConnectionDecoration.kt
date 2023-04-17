package com.baseapp.main.special_rc.connection_rc

import android.graphics.*
import com.base.common.getBaseActOrAppContext
import com.base.common.util.dp2px
import com.base.common.view.SimpleItemDecoration
import com.baseapp.R

class ConnectionDecoration : SimpleItemDecoration() {
    override var decorationHeight: Int = dp2px(8f).toInt()

    private val icon = BitmapFactory.decodeResource(getBaseActOrAppContext().resources, R.mipmap.transit_icon)

    //图片位置矩形
    private val rectF = RectF(0f, 0f, 0f, 0f)

    /**
     * 画笔
     */
    private val mPaint: Paint = Paint()

    init {
        mPaint.isAntiAlias = true
    }

    override fun offsets(outRect: Rect, childPosition: Int) {
        if (childPosition != 0) {
            outRect.set(0, decorationHeight, 0, 0)
        }
    }

    override fun drawDecoration(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, childPosition: Int) {}

    override fun drawOverTop(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, firstVisiblePosition: Int) {
    }

    override fun drawOverDecoration(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, childPosition: Int) {
        if (childPosition != 0) {
            val dis = (right - left) / 3

            drawBitmap(left + dis, top, c)
            drawBitmap(left + dis * 2, top, c)
        }
    }

    private fun drawBitmap(startX: Float, top: Float, c: Canvas) {
        val x = startX - icon.width / 2
        val y = top + decorationHeight / 2 - icon.height / 2
        rectF.set(
            x,
            y,
            x + icon.width,
            y + icon.height
        )
        c.drawBitmap(icon, null, rectF, mPaint)
    }
}