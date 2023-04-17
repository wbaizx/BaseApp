package com.baseapp.main.special_rc.indicator

import android.view.ViewGroup
import android.widget.ImageView
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.BaseRecycleAdapter
import com.base.common.base.adapter.mackTestListData
import com.base.common.util.imageload.imgUrl
import com.base.common.util.imageload.loadBlurImg
import com.baseapp.R

class IndicatorAdapter : BaseRecycleAdapter<String, BaseHolder>() {
    init {
        mackTestListData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.item_indicator_item)

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        (holder.itemView as ImageView).loadBlurImg(imgUrl)
    }
}