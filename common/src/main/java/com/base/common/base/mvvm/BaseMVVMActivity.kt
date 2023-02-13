package com.base.common.base.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.base.common.base.BaseActivity

abstract class BaseMVVMActivity<VM : BaseMVVMViewModel, B : ViewDataBinding> : BaseActivity() {
    abstract val vm: VM
    private lateinit var binding: B

    override fun bindView(inflater: LayoutInflater, container: ViewGroup): View {
        //因为基类采用add方式添加的content布局，所以这里通过这种方式绑定DataBinding
        binding = DataBindingUtil.inflate(inflater, getContentView(), container, false)
        binding.lifecycleOwner = this
        bindModelId(binding)
        return binding.root
    }

    /**
     * 绑定viewModel到UI
     */
    protected abstract fun bindModelId(binding: B)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBaseObserve()
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