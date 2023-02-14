package com.baseapp.main.mvvm

import com.base.common.base.activity.BaseBindModelActivity
import com.base.common.base.adapter.BaseViewPagerAdapter
import com.base.common.extension.setOnSingleClickListener
import com.base.common.util.lifecycleCollect
import com.base.common.util.log
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
        log(TAG, "viewModel ${vm.hashCode()}")

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
        vm.name.lifecycleCollect(this) {
            log(TAG, "name ${this.hashCode()}")
        }
    }

    override fun onDestroy() {
        log(TAG, "onDestroy ${this.hashCode()}")
        super.onDestroy()
    }

}