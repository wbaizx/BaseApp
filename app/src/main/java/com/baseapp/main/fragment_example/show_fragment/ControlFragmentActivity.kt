package com.baseapp.main.fragment_example.show_fragment

import com.base.common.base.activity.BaseBindContentActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityControlFragmentBinding

class ControlFragmentActivity : BaseBindContentActivity<ActivityControlFragmentBinding>() {
    private lateinit var fragmentControl: FragmentControl

    override fun getContentView() = R.layout.activity_control_fragment

    override fun viewBind(binding: ActivityControlFragmentBinding) {}

    override fun initView() {
        fragmentControl = FragmentControl(supportFragmentManager, R.id.frameLayout)

        binding.simpleTabLayout.setListener {
            fragmentControl.show(it)
        }

        binding.reset.setOnClickListener {
            fragmentControl.reset()
            fragmentControl = FragmentControl(supportFragmentManager, R.id.frameLayout)
            fragmentControl.show(0)
            binding.simpleTabLayout.setPosition(0)
        }

        binding.simpleTabLayout.setData(arrayListOf("第1个", "第2个", "第3个", "第4个", "第5个", "第6个", "第7个"))
        fragmentControl.show(0)
    }
}
