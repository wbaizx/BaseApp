package com.baseapp.main.item_animation.item_animation2

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.base.common.base.activity.BaseBindContentActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityItemAnimation2Binding

class ItemAnimation2Activity : BaseBindContentActivity<ActivityItemAnimation2Binding>() {
    private var isLine = true

    override fun getContentView() = R.layout.activity_item_animation2

    override fun viewBind(binding: ActivityItemAnimation2Binding) {}

    override fun initView() {
        val adapter = ItemAnimation2Adapter()
        val layoutManager = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = MyDefaultItemAnimator().apply {
            changeDuration = 600
            addDuration = 600
        }


        binding.change.setOnClickListener {
            if (isLine) {
                adapter.setLine(false)
                layoutManager.orientation = GridLayoutManager.VERTICAL
                layoutManager.spanCount = 2
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
            } else {
                adapter.setLine(true)
                layoutManager.orientation = GridLayoutManager.HORIZONTAL
                layoutManager.spanCount = 1
                adapter.notifyItemRangeChanged(0, adapter.itemCount)
            }
            isLine = !isLine

        }
    }
}
