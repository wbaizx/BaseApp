package com.baseapp.main.coordinator.coordinator1

import com.base.common.base.adapter.BaseListAdapter
import com.baseapp.R
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class Coordinator1Adapter : BaseListAdapter<String, BaseViewHolder>(R.layout.item_default_layout) {
    init {
        repeat(20) {
            data.add("啦啦")
        }
    }

    override fun convertUI(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.item_text, "$item  ${holder.bindingAdapterPosition - headerLayoutCount}")
    }
}