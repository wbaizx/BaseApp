package com.baseapp.main.special_rc.slide_rc

import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.base.adapter.mackTestListData
import com.baseapp.R
import com.baseapp.databinding.ActivitySlideBinding

class SlideRCActivity : BaseBindContentActivity<ActivitySlideBinding>() {
    override fun getContentView() = R.layout.activity_slide

    override fun viewBind(binding: ActivitySlideBinding) {
    }

    override fun initView() {
        SlidAdapter().apply { mackTestListData() }.bind(binding.recyclerView)
    }
}