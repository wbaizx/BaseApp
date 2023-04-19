package com.baseapp.main.special_rc.slide_rc

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * easy to expand with translation
 */
class ListSlidMenuItem(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    companion object {
        const val EXPAND = "EXPAND"
        const val EXPAND_ING = "EXPAND_ING"
        const val FOLD = "FOLD"

        private const val animateTime = 200L
    }

    private var startX = 0f
    private var curTranslationX = 0f
    private var isDrag = false

    private lateinit var contentView: View
    private lateinit var menuView: View
    private var isInit = false

    private val interpolator by lazy { LinearInterpolator() }

    var state = FOLD
    var slidListener: SlidListener? = null

    interface SlidListener {
        fun onDownEvent() {}
        fun onExpand() {}
        fun onFold() {}
    }

    init {
        orientation = HORIZONTAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        contentView = getChildAt(0)
        menuView = getChildAt(1)
        isInit = true
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                curTranslationX = menuView.translationX
                slidListener?.onDownEvent()
            }
            MotionEvent.ACTION_MOVE -> {
                if (abs(startX - ev.x) > 10 && !isDrag) {
                    isDrag = true
                    state = EXPAND_ING
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDrag = false
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?) = isDrag

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_MOVE -> {
                slidTo(getSlidToLocation(startX - ev.x))
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                slidReset()
            }
        }
        return true
    }

    private fun slidReset() {
        val maxSlid = menuView.width.toFloat()
        if (-menuView.translationX < maxSlid / 2) {
            smoothFoldMenu()
            slidListener?.onFold()
        } else {
            smoothExpandMenu()
            slidListener?.onExpand()
        }
    }

    /**
     * fl positive: finger slid left
     * fl negative: finger slid right
     */
    private fun getSlidToLocation(fl: Float): Float {
        val maxSlid = menuView.width.toFloat()
        return min(0f, max(curTranslationX - fl, -maxSlid))
    }

    private fun slidTo(to: Float) {
        contentView.translationX = to
        menuView.translationX = to
    }

    private fun smoothSlidTo(to: Float) {
        if (contentView.translationX == to && menuView.translationX == to) {
            return
        }
        contentView.animate().translationX(to).setInterpolator(interpolator).setDuration(animateTime).start()
        menuView.animate().translationX(to).setInterpolator(interpolator).setDuration(animateTime).start()
    }

    private fun smoothExpandMenu() {
        safeExecute {
            smoothSlidTo(-menuView.width.toFloat())
            state = EXPAND
        }
    }

    fun smoothFoldMenu() {
        safeExecute {
            smoothSlidTo(0f)
            state = FOLD
        }
    }

    fun expandMenu() {
        safeExecute {
            slidTo(-menuView.width.toFloat())
            state = EXPAND
        }
    }

    fun foldMenu() {
        safeExecute {
            slidTo(0f)
            state = FOLD
        }
    }

    private inline fun safeExecute(crossinline action: () -> Unit) {
        if (isInit) {
            action()
        } else {
            post { action() }
        }
    }
}