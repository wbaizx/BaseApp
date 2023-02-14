package com.baseapp.main.item_animation

import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.util.launchActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityItemAnimationMainBinding
import com.baseapp.main.item_animation.item_animation1.ItemAnimation1Activity
import com.baseapp.main.item_animation.item_animation2.ItemAnimation2Activity
import com.baseapp.main.item_animation.item_animation3.ItemAnimation3Activity

class ItemAnimationMainActivity : BaseBindContentActivity<ActivityItemAnimationMainBinding>() {

    override fun getContentView() = R.layout.activity_item_animation_main

    override fun viewBind(binding: ActivityItemAnimationMainBinding) {}

    override fun initView() {
        binding.itemAnimation1.setOnClickListener {
            launchActivity(this, ItemAnimation1Activity::class.java)
        }

        binding.itemAnimation2.setOnClickListener {
            launchActivity(this, ItemAnimation2Activity::class.java)
        }

        binding.itemAnimation3.setOnClickListener {
            launchActivity(this, ItemAnimation3Activity::class.java)
        }
    }
}
