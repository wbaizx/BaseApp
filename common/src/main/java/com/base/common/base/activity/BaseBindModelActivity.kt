package com.base.common.base.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.base.common.base.BaseViewModel
import com.base.common.helper.stateFlowLifecycleCollect

abstract class BaseBindModelActivity<VM : BaseViewModel, B : ViewDataBinding> : BaseBindContentActivity<B>() {
    abstract val vm: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBaseObserve()
        initObserve()
    }

    private fun initBaseObserve() {
        vm.showLoad.stateFlowLifecycleCollect(this) {
            if (it) {
                showLoadDialog()
            } else {
                hideLoadDialog()
            }
        }
    }

    protected abstract fun initObserve()
}