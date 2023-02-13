package com.baseapp.main.mvvm

import com.base.common.base.adapter.BaseViewPagerAdapter
import com.base.common.base.mvvm.BaseMVVMActivity
import com.base.common.extension.setOnSingleClickListener
import com.base.common.util.log
import com.baseapp.R
import com.baseapp.databinding.ActivityMvvmDemoBinding
import kotlinx.android.synthetic.main.activity_mvvm_demo.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "MVVMDemoActivity"

class MVVMDemoActivity : BaseMVVMActivity<MVVMDemoViewModel, ActivityMvvmDemoBinding>() {
    /**
     * viewModel 使用koin注入方式
     */
    override val vm by viewModel<MVVMDemoViewModel>()

    override fun getContentView() = R.layout.activity_mvvm_demo

    override fun bindModelId(binding: ActivityMvvmDemoBinding) {
        binding.vm = vm
    }

    override fun initView() {
        log(TAG, "viewModel ${vm.hashCode()}")

        save.setOnClickListener {
            vm.saveData()
        }

        query.setOnSingleClickListener {
            vm.queryData()
        }

        viewPager2.adapter = BaseViewPagerAdapter(
            this, arrayListOf(
                MVVMDemoFragment(),
                MVVMDemoFragment()
            )
        )
    }

    override fun initObserve() {
        vm.name.observe(this) {
            log(TAG, "name ${this.hashCode()}")
        }
    }

    override fun onDestroy() {
        log(TAG, "onDestroy ${this.hashCode()}")
        super.onDestroy()
    }
}