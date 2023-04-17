package com.baseapp.main.item_animation.item_animation2

import android.view.ViewGroup
import android.widget.TextView
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.BaseRecycleAdapter
import com.base.common.base.adapter.mackTestListData
import com.base.common.util.dp2px
import com.base.common.util.getScreenShowHeight
import com.baseapp.R

class ItemAnimation2Adapter : BaseRecycleAdapter<String, BaseHolder>() {
    private var isLine = true

    init {
        mackTestListData()
    }

    fun setLine(isLine: Boolean) {
        this.isLine = isLine
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.item_anim2_layout_item)

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val view = holder.itemView.findViewById<TextView>(R.id.sceneText)
        view.text = getItem(position)
        if (isLine) {
            view.layoutParams.height = getScreenShowHeight() - dp2px(200f).toInt()
        } else {
            view.layoutParams.height = dp2px(160f).toInt()
        }
    }
}