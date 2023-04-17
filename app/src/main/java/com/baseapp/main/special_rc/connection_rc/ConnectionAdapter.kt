package com.baseapp.main.special_rc.connection_rc

import android.view.ViewGroup
import android.widget.TextView
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.BaseRecycleAdapter
import com.base.common.base.adapter.mackTestListData
import com.baseapp.R

class ConnectionAdapter : BaseRecycleAdapter<String, BaseHolder>() {
    init {
        mackTestListData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.item_default_layout)

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.item_text).text = "${getItem(position)} ${holder.bindingAdapterPosition}"
    }
}