package com.baseapp.main.item_animation.item_animation1

import android.view.ViewGroup
import android.widget.TextView
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.BaseRecycleAdapter
import com.baseapp.R

class ItemAnimation1Adapter : BaseRecycleAdapter<String, BaseHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.item_default_layout)

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.item_text).text = "${getItem(position)} ${holder.bindingAdapterPosition}"
    }
}