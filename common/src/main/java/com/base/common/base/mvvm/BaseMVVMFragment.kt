package com.base.common.base.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.base.common.base.BaseFragment

abstract class BaseMVVMFragment<VM : BaseMVVMViewModel, B : ViewDataBinding> : BaseFragment() {
    abstract val vm: VM
    private lateinit var binding: B

    override fun bindView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, getContentView(), container, false)
        binding.lifecycleOwner = this
        bindModelId(binding)

        return binding.root
    }

    /**
     * 绑定viewModel到UI
     */
    abstract fun bindModelId(binding: B)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //如果采用activityViewModels共用viewModel，那么统一交给activity注册的基本监听接收
        //在fragment中不需要注册基本监听,以免重复接收
        if ((activity as? BaseMVVMActivity<*, *>)?.vm != vm) {
            initBaseObserve()
        }
        initObserve()
    }

    private fun initBaseObserve() {
        vm.showLoad.observe(this) {
            if (it) {
                showLoadDialog()
            } else {
                hideLoadDialog()
            }
        }
    }

    protected abstract fun initObserve()

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }
}