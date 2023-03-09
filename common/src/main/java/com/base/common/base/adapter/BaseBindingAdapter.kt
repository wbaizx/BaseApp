package com.base.common.base.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.base.common.util.debugLog
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

abstract class BaseBindingAdapter<M, B : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    BaseListAdapter<M, BaseDataBindingHolder<B>>(layoutResId) {
    private val TAG = "BaseBindingAdapter"

    init {
        debugLog(TAG, "init")
    }

    override fun configure(holder: BaseDataBindingHolder<B>, item: M) {
        val binding = holder.dataBinding
        if (binding != null) {
            bindModelId(binding, item)
            binding.executePendingBindings()
        } else {
            debugLog(TAG, "binding = null")
        }
    }

    /**
     * 绑定viewModel到UI
     */
    abstract fun bindModelId(binding: B, item: M)
}