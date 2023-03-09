package com.baseapp.main.coordinator.coordinator1

import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.util.debugLog
import com.baseapp.R
import com.baseapp.databinding.ActivityCoordinator1Binding
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
        val coordinator1Adapter = Coordinator1Adapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addItemDecoration(Coordinator1Decoration(coordinator1Adapter))
        binding.recyclerView.adapter = coordinator1Adapter

        val headerView: View = LayoutInflater.from(this).inflate(
            R.layout.default_header_layout,
            binding.recyclerView,
            false
        )
        coordinator1Adapter.addHeaderView(headerView)

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
