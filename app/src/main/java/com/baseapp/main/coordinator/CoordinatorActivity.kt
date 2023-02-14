package com.baseapp.main.coordinator

import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.util.launchActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityCoordinatorBinding
import com.baseapp.main.coordinator.coordinator1.Coordinator1Activity
import com.baseapp.main.coordinator.coordinator2.Coordinator2Activity

class CoordinatorActivity : BaseBindContentActivity<ActivityCoordinatorBinding>() {

    override fun getContentView() = R.layout.activity_coordinator

    override fun viewBind(binding: ActivityCoordinatorBinding) {}

    override fun initView() {
        binding.coordinator1.setOnClickListener {
            launchActivity(this, Coordinator1Activity::class.java)
        }

        binding.coordinator2.setOnClickListener {
            launchActivity(this, Coordinator2Activity::class.java)
        }
    }
}
