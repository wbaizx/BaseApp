package com.baseapp.main.special_rc.damping_rc

import com.base.common.base.adapter.BaseListAdapter
import com.base.common.util.dp2px
import com.base.common.util.getScreenRealHeight
import com.baseapp.R
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 这里直接设置的高度，也可以通过minimumHeight设置最小高度实现全屏
 * 注意全面屏和刘海屏适配
 */
class DampingRCAdapter : BaseListAdapter<String, BaseViewHolder>(R.layout.damping_rc_item) {
    init {
        data = arrayListOf("", "", "", "", "", "", "")
    }

    override fun convertUI(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.item_text, "${holder.bindingAdapterPosition - headerLayoutCount}")
        if (holder.bindingAdapterPosition - headerLayoutCount == 2 || holder.bindingAdapterPosition - headerLayoutCount == 3) {
            holder.itemView.layoutParams.height = getScreenRealHeight()

        } else {
            holder.itemView.layoutParams.height = dp2px(900f).toInt()
        }
    }
}