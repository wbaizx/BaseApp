package com.baseapp.main.special_rc.circle_rc

import androidx.recyclerview.widget.LinearLayoutManager
import com.base.common.base.activity.BaseBindContentActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityCircleRcBinding

class CircleRCActivity : BaseBindContentActivity<ActivityCircleRcBinding>() {

    override fun getContentView() = R.layout.activity_circle_rc

    override fun viewBind(binding: ActivityCircleRcBinding) {}

    override fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CircleAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(CircleDecoration(adapter))
    }
}
