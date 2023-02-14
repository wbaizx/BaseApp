package com.baseapp.main.fragment_example

import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.util.launchActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityFragmentExampleBinding
import com.baseapp.main.fragment_example.show_fragment.ControlFragmentActivity
import com.baseapp.main.fragment_example.vp_fragment.VP2FragmentActivity

class FragmentExampleActivity : BaseBindContentActivity<ActivityFragmentExampleBinding>() {

    override fun getContentView() = R.layout.activity_fragment_example

    override fun viewBind(binding: ActivityFragmentExampleBinding) {}

    override fun initView() {
        binding.vp2Fragment.setOnClickListener {
            launchActivity(this, VP2FragmentActivity::class.java)
        }

        binding.showFragment.setOnClickListener {
            launchActivity(this, ControlFragmentActivity::class.java)
        }
    }
}
