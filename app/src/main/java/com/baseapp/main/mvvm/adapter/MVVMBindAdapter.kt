package com.baseapp.main.mvvm.adapter

import android.content.Context
import android.view.ViewGroup
import com.base.common.base.adapter.BaseBindHolder
import com.baseapp.R
import com.baseapp.databinding.MvvmBindItemBinding
import com.chad.library.adapter.base.BaseQuickAdapter

class MVVMBindAdapter : BaseQuickAdapter<MVVMBindBean, BaseBindHolder<MvvmBindItemBinding>>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int) = BaseBindHolder<MvvmBindItemBinding>(parent, R.layout.mvvm_bind_item)

    override fun onBindViewHolder(holder: BaseBindHolder<MvvmBindItemBinding>, position: Int, item: MVVMBindBean?) {
        holder.binding?.bean = item
        holder.binding?.executePendingBindings()
    }
}