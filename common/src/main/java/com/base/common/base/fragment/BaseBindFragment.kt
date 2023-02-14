package com.base.common.base.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindFragment<B : ViewDataBinding> : BaseFragment() {

    protected lateinit var binding: B

    override fun loadViewLayout(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, getContentView(), container, false)
        binding.lifecycleOwner = this
        viewBind(binding)
        return binding.root
    }

    protected abstract fun viewBind(binding: B)

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }
}