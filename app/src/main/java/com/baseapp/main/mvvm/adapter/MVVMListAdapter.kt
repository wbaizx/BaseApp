package com.baseapp.main.mvvm.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.base.common.base.adapter.BaseHolder
import com.baseapp.R
import com.chad.library.adapter.base.BaseQuickAdapter

class MVVMListAdapter : BaseQuickAdapter<MVVMBindBean, BaseHolder>() {

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.mvvm_list_item)

    override fun onBindViewHolder(holder: BaseHolder, position: Int, item: MVVMBindBean?) {
        holder.itemView.findViewById<TextView>(R.id.tex).text = item?.id
    }
}