package com.baseapp.main.mvvm

import com.base.common.base.activity.BaseBindModelActivity
import com.base.common.base.adapter.BaseViewPagerAdapter
import com.base.common.helper.setOnSingleClickListener
import com.base.common.helper.distinctLifecycleCollect
import com.base.common.util.debugLog
import com.baseapp.R
import com.baseapp.databinding.ActivityMvvmDemoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "MVVMDemoActivity"

class MVVMDemoActivity : BaseBindModelActivity<MVVMDemoViewModel, ActivityMvvmDemoBinding>() {
    /**
     * viewModel 使用koin注入方式
     */
    override val vm by viewModel<MVVMDemoViewModel>()

    override fun getContentView() = R.layout.activity_mvvm_demo

    override fun viewBind(binding: ActivityMvvmDemoBinding) {
        binding.vm = vm
    }

    override fun initView() {
        debugLog(TAG, "viewModel ${vm.hashCode()}")

        binding.save.setOnClickListener {
            vm.saveData()
        }

        binding.query.setOnSingleClickListener {
            vm.queryData()
        }

        binding.viewPager2.adapter = BaseViewPagerAdapter(
            this, arrayListOf(
                MVVMDemoFragment(),
                MVVMDemoFragment()
            )
        )
    }

    override fun initObserve() {
        vm.name.distinctLifecycleCollect(this) {
            debugLog(TAG, "name ${this.hashCode()}")
        }
    }

    override fun onDestroy() {
        debugLog(TAG, "onDestroy ${this.hashCode()}")
        super.onDestroy()
    }

}