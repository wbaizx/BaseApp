package com.base.common.base.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindActivity<B : ViewDataBinding> : BaseActivity() {

    protected lateinit var binding: B

    override fun loadViewLayout() {
        binding = DataBindingUtil.setContentView(this, getContentView())
        binding.lifecycleOwner = this
        viewBind(binding)
    }

    protected abstract fun viewBind(binding: B)

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }
}