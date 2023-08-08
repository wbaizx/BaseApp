package com.base.common.base.fragment

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.base.common.base.BaseViewModel
import com.base.common.base.activity.BaseBindModelActivity
import com.base.common.helper.distinctLifecycleCollect

abstract class BaseBindModelFragment<VM : BaseViewModel, B : ViewDataBinding> : BaseBindFragment<B>() {
    abstract val vm: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //如果采用activityViewModels共用viewModel，那么统一交给activity注册的基本监听接收
        //在fragment中不需要注册基本监听,以免重复接收
        if ((activity as? BaseBindModelActivity<*, *>)?.vm != vm) {
            initBaseObserve()
        }
        initObserve()
    }

    private fun initBaseObserve() {
        vm.showLoad.distinctLifecycleCollect(this) {
            if (it) {
                showLoadDialog()
            } else {
                hideLoadDialog()
            }
        }
    }

    protected abstract fun initObserve()
}