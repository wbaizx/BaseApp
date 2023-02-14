package com.ndk.home

import com.alibaba.android.arouter.facade.annotation.Route
import com.base.common.base.activity.BaseBindContentActivity
import com.ndk.R
import com.ndk.databinding.ActivityNdkHomeBinding

@Route(path = "/ndk/ndk_home", name = "NDK模块首页")
class NDKHomeActivity : BaseBindContentActivity<ActivityNdkHomeBinding>() {
    override fun getContentView() = R.layout.activity_ndk_home

    override fun initView() {
        binding.text.text = NDKHelper.stringFromJNI()
        JavaTest().start()
    }

    override fun viewBind(binding: ActivityNdkHomeBinding) {
    }
}