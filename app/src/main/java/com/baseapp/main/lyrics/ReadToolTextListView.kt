package com.baseapp.main.lyrics

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.base.common.util.TimeStatisticsUtil

private const val ANIMATOR_DURATION = 500L

class ReadToolTextListView(context: Context, attrs: AttributeSet?) : NestedScrollView(context, attrs) {

    private val timeStatisticsUtil = TimeStatisticsUtil()
    private val processHandler = Handler(Looper.getMainLooper())
    private var layout: LinearLayout = LinearLayout(context)
    private var objectAnimator: ObjectAnimator? = null

    //当前选中段落的下标
    private var currentParagraphIndex = 0

    //当前选中段落的行的下标
    private var currentLineIndex = 0

    init {
        layout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layout.orientation = LinearLayout.VERTICAL
        addView(layout)
    }

    fun updateLayout() {
        post {
            layout.removeAllViews()

            val topView = TextView(context)
            topView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, measuredHeight / 2)
            layout.addView(topView)

            var time = 0L
            repeat(10) {
                val readToolTextView = ReadToolTextView(context)
                val textBeans = arrayListOf<TextBean>()
                repeat((1..20).random()) {
                    textBeans.add(TextBean("你", "ni", time, time + 100))
                    time += 100 + 100
                }
                readToolTextView.setData(textBeans)
                layout.addView(readToolTextView)

                val random = (1..2).random()
                if (random == 1) {
                    val t1 = TextView(context)
                    t1.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200)
                    t1.text = "分割------------"
                    layout.addView(t1)
                }
            }

            val bottomView = TextView(context)
            bottomView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, measuredHeight / 2)
            layout.addView(bottomView)
        }
    }

    fun startProgress() {
        timeStatisticsUtil.reset()
        timeStatisticsUtil.start()

        processHandler.removeCallbacks(runnable)
        post {
            processHandler.post(runnable)
        }
    }

    fun pauseProgress() {
        timeStatisticsUtil.pause()
        processHandler.removeCallbacks(runnable)
    }

    fun resumeProgress() {
        timeStatisticsUtil.resume()
        processHandler.removeCallbacks(runnable)
        post {
            processHandler.post(runnable)
        }
    }

    fun stopProgress() {
        processHandler.removeCallbacks(runnable)
    }

    private fun progressEnd() {
        stopProgress()

        val readToolTextView = layout.getChildAt(currentParagraphIndex) as? ReadToolTextView
        readToolTextView?.changeSizeToMin()
    }

    private val runnable = object : Runnable {
        override fun run() {
            val duration = timeStatisticsUtil.getDuration()

            //从当前位置currentParagraphIndex 开始循环
            for (paragraphIndex in currentParagraphIndex until layout.childCount) {
                val itemView = layout.getChildAt(paragraphIndex) as? ReadToolTextView
                if (itemView == null) {
                    if (paragraphIndex == layout.childCount - 1) {
                        //已经循环到末尾，且该view不是ReadToolTextView
                        progressEnd()
                        return
                    }
                    continue
                }
                //定位段逻辑，返回现在或将来是否存在某个段落能够匹配当前时间
                val isExist = findParagraph(duration, paragraphIndex, itemView)

                if (isExist) break

                //如果现在或将来不存在某个段落能够匹配当前时间，且已经循环到末尾
                if (!isExist && paragraphIndex == layout.childCount - 1) {
                    progressEnd()
                    return
                }
            }

            processHandler.postDelayed(this, 50)
        }
    }

    /**
     * 寻找对应段落
     * 返回值：是否存在段落
     */
    private fun findParagraph(duration: Long, paragraphIndex: Int, newReadToolTextView: ReadToolTextView): Boolean {
        val textBeans = newReadToolTextView.textBeans
        val paragraphStartTime = textBeans[0].startTime
        val paragraphEndTime = textBeans[textBeans.size - 1].endTime

        if (paragraphStartTime == null || paragraphEndTime == null) return false

        //如果当前时间小于该段起始时间，那未来该段落一定能匹配，所以返回true
        if (duration < paragraphStartTime) return true

        if (duration in paragraphStartTime..paragraphEndTime) {
            //如果index不等，说明首次触发，所以定位到该段第一行，同时执行缩放动画
            if (currentParagraphIndex != paragraphIndex) {
                val oldReadToolTextView = layout.getChildAt(currentParagraphIndex) as? ReadToolTextView
                oldReadToolTextView?.changeSizeToMin()
                newReadToolTextView.changeSizeToMax()
                //重置标志位
                currentParagraphIndex = paragraphIndex
                currentLineIndex = 0

                scrollTo(newReadToolTextView.top - measuredHeight / 2 + newReadToolTextView.getLineCenterTopForMax(0))

            } else {
                //定位到行
                findLine(duration, textBeans, newReadToolTextView)
            }
            return true
        }

        return false
    }

    /**
     * 寻找对应行
     */
    private fun findLine(duration: Long, textBeans: ArrayList<TextBean>, newReadToolTextView: ReadToolTextView) {
        for ((lineIndex, lineData) in newReadToolTextView.lineDataList.withIndex()) {
            val lineStartTime = textBeans[lineData.textList[0].pos].startTime
            val lineEntTime = textBeans[lineData.textList[lineData.textList.size - 1].pos].startTime

            if (lineStartTime == null || lineEntTime == null) continue

            if (duration in lineStartTime..lineEntTime) {
                if (currentLineIndex != lineIndex) {
                    currentLineIndex = lineIndex
                    scrollTo(newReadToolTextView.top - measuredHeight / 2 + newReadToolTextView.getLineCenterTopForMax(lineIndex))
                }
                break
            }
        }
    }

    /**
     * 滚动到指定位置
     */
    private fun scrollTo(toY: Int) {
        closeAnimator()
        objectAnimator = ObjectAnimator.ofInt(this, "scrollY", toY).apply {
            duration = ANIMATOR_DURATION
            start()
        }
    }

    private fun closeAnimator() {
        objectAnimator?.run {
            if (isRunning) cancel()
        }
    }

    override fun onDetachedFromWindow() {
        stopProgress()
        closeAnimator()
        super.onDetachedFromWindow()
    }
}