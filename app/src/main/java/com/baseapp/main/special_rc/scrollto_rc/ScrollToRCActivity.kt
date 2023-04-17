package com.baseapp.main.special_rc.scrollto_rc

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.BaseRecycleAdapter
import com.base.common.util.debugLog
import com.baseapp.R
import com.baseapp.databinding.ActivityScrollToRcBinding

class ScrollToRCActivity : BaseBindContentActivity<ActivityScrollToRcBinding>() {
    private val TAG = "ScrollToRCActivity"

    private val manager = LinearLayoutManager(this)
    private val adapter = object : BaseRecycleAdapter<String, BaseHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.scorll_to_rc_layout)

        override fun onBindViewHolder(holder: BaseHolder, position: Int) {
            holder.itemView.findViewById<TextView>(R.id.item_text).text = "${getItem(position)} ${holder.bindingAdapterPosition}"
        }
    }
    private val decoration = ScrollToRCDecoration()

    override fun getContentView() = R.layout.activity_scroll_to_rc

    override fun viewBind(binding: ActivityScrollToRcBinding) {}

    override fun initView() {
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(decoration)

        tabLayoutBind()

        val data = arrayListOf<String>()
        repeat(20) {
            data.add("第 $it 个")
        }
        adapter.submitList(data)
        binding.simpleTabLayout.setData(data)
    }

    private fun tabLayoutBind() {
        var needOverseeScroll = true
        var move = false
        var toPos = -1

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        move = false
                        needOverseeScroll = true
                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                    }
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        if (!move) {
                            needOverseeScroll = true
                        }
                    }
                }
                debugLog(TAG, "onScrollStateChanged $newState")
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (needOverseeScroll) {
                    binding.simpleTabLayout.setPosition(manager.findFirstVisibleItemPosition())
                } else {
                    if (move && manager.findLastVisibleItemPosition() == toPos) {
                        recyclerView.stopScroll()

                        move = false
                        //要置顶的项已经在屏幕上
                        val top = manager.findViewByPosition(toPos)!!.top - decoration.decorationHeight
                        recyclerView.smoothScrollBy(0, top)
                    }
                }
            }
        })

        binding.simpleTabLayout.setListener { pos ->
            needOverseeScroll = false

            val first = manager.findFirstVisibleItemPosition()
            val last = manager.findLastVisibleItemPosition()
            when {
                pos < first -> {
                    //当要置顶的项在当前显示的第一个项的前面时
                    binding.recyclerView.smoothScrollToPosition(pos)
                }
                pos > last -> {
                    //这两变量是用在RecyclerView滚动监听里面的
                    move = true
                    toPos = pos
                    //当要置顶的项在当前显示的最后一项的后面时
                    binding.recyclerView.smoothScrollToPosition(pos)
                }
                else -> {
                    //当要置顶的项已经在屏幕上显示时
                    val top = manager.findViewByPosition(pos)!!.top - decoration.decorationHeight
                    binding.recyclerView.smoothScrollBy(0, top)
                }
            }
        }
    }
}
