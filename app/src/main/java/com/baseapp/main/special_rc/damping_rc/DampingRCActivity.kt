package com.baseapp.main.special_rc.damping_rc

import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.util.dp2px
import com.base.common.util.log
import com.baseapp.R
import com.baseapp.databinding.ActivityDampingRcBinding
import com.gyf.immersionbar.ImmersionBar

/**
 * 需要注意全面屏和刘海屏适配
 */

private const val TAG = "DampingRCActivity"

class DampingRCActivity : BaseBindContentActivity<ActivityDampingRcBinding>() {

    private val manager = DampingLinearLayoutManager(this)

    override fun getContentView() = R.layout.activity_damping_rc

    override fun viewBind(binding: ActivityDampingRcBinding) {}

    override fun setImmersionBar() {
        ImmersionBar.with(this)
//            .statusBarColor(R.color.color_black)
//            .fitsSystemWindows(true)
            .init()
    }

    override fun initView() {
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = DampingRCAdapter()

        manager.barHeight = dp2px(80f)

        manager.setOffsetListener { upOffset, downOffset ->
            //upOffset 控制脚部偏移
            //downOffset 控制头部偏移
            log(TAG, "Offset  $upOffset --- $downOffset")
            binding.footer.translationY = -upOffset
            binding.header.translationY = downOffset
        }

        manager.setPageListener { page ->
            binding.footer.text = "$page footer"
            binding.header.text = "$page header"
            log(TAG, "Page  $page")
        }
    }
}
