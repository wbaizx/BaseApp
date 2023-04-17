package com.baseapp.main.coordinator.coordinator1

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.mackTestListData
import com.base.common.util.debugLog
import com.baseapp.R
import com.baseapp.databinding.ActivityCoordinator1Binding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.chad.library.adapter.base.QuickAdapterHelper
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import kotlin.math.abs

/**
 * 注意!
 * 折叠布局下方的滚动子View如果是嵌套recyclerView，会影响折叠布局滚动顺序逻辑
 */

private const val TAG = "Coordinator1Activity"

class Coordinator1Activity : BaseBindContentActivity<ActivityCoordinator1Binding>() {

    override fun getContentView() = R.layout.activity_coordinator1

    override fun viewBind(binding: ActivityCoordinator1Binding) {}

    override fun setImmersionBar() {
        ImmersionBar.with(this)
            .titleBar(R.id.toolbar)
            .init()
    }

    override fun initView() {
        val coordinator1Adapter = object : BaseQuickAdapter<String, BaseHolder>() {
            override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.item_default_layout)

            override fun onBindViewHolder(holder: BaseHolder, position: Int, item: String?) {
                holder.itemView.findViewById<TextView>(R.id.item_text).text = "$item ${holder.bindingAdapterPosition}"
            }
        }.apply { this.mackTestListData() }

        val helper = QuickAdapterHelper.Builder(coordinator1Adapter).build()
        helper.addBeforeAdapter(object : BaseSingleItemAdapter<String, BaseHolder>() {
            override fun onBindViewHolder(holder: BaseHolder, item: String?) {
            }

            override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.default_header_layout)

        }.apply { item = "" })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(Coordinator1Decoration(coordinator1Adapter))
        binding.recyclerView.adapter = helper.adapter

        binding.appBar.post {
            val layoutParams = binding.appBar.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = layoutParams.behavior as AppBarLayout.Behavior
            behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
                override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                    return true
                }
            })
        }

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBar, offset ->
            val off = abs(offset.toFloat())
            binding.toolbar.alpha = off / appBar.totalScrollRange
            binding.testArea.y = appBar.totalScrollRange.toFloat() + binding.toolbar.paddingTop
            binding.testArea.translationX = -off
            debugLog(TAG, "addOnOffsetChangedListener $off  -  ${appBar.totalScrollRange}")
        })

        binding.toolbarImg.setOnClickListener {
            debugLog(TAG, "toolbarImg click")
        }

        binding.testArea.setOnClickListener {
            debugLog(TAG, "testArea click")
        }
    }
}
