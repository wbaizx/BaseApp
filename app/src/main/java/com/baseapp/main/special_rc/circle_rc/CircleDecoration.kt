package com.baseapp.main.special_rc.circle_rc

import android.graphics.Canvas
import android.graphics.Rect
import com.base.common.util.dp2px
import com.base.common.view.SimpleItemDecoration

class CircleDecoration : SimpleItemDecoration() {

    override var decorationHeight: Int = 0

    override fun offsets(outRect: Rect, childPosition: Int) {
        if (childPosition != 0) {
            outRect.left = -dp2px(22f).toInt()
        }
    }

    override fun drawDecoration(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, childPosition: Int) {
    }

    override fun drawOverTop(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, firstVisiblePosition: Int) {
    }

    override fun drawOverDecoration(c: Canvas, left: Float, right: Float, top: Float, bottom: Float, childPosition: Int) {
    }
}