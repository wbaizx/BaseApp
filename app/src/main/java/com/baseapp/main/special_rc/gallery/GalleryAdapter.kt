package com.baseapp.main.special_rc.gallery

import android.view.ViewGroup
import android.widget.TextView
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.BaseRecycleAdapter
import com.base.common.base.adapter.mackTestListData
import com.baseapp.R

class GalleryAdapter : BaseRecycleAdapter<String, BaseHolder>() {
    init {
        this.mackTestListData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.item_gallery_layout_item)

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.item_text).text = "${getItem(position)} ${holder.bindingAdapterPosition}"
    }
}