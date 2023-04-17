package com.baseapp.main.special_rc.damping_rc

import android.view.ViewGroup
import android.widget.TextView
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.BaseRecycleAdapter
import com.base.common.base.adapter.mackTestListData
import com.base.common.util.dp2px
import com.base.common.util.getScreenRealHeight
import com.baseapp.R

/**
 * 这里直接设置的高度，也可以通过minimumHeight设置最小高度实现全屏
 * 注意全面屏和刘海屏适配
 */
class DampingRCAdapter : BaseRecycleAdapter<String, BaseHolder>() {
    init {
        this.mackTestListData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.damping_rc_item)

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.item_text).text = "${getItem(position)} ${holder.bindingAdapterPosition}"
        if (holder.bindingAdapterPosition == 2 || holder.bindingAdapterPosition == 3) {
            holder.itemView.layoutParams.height = getScreenRealHeight()

        } else {
            holder.itemView.layoutParams.height = dp2px(900f).toInt()
        }
    }
}