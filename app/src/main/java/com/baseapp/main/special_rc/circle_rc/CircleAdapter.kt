package com.baseapp.main.special_rc.circle_rc

import android.view.ViewGroup
import android.widget.ImageView
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.BaseRecycleAdapter
import com.base.common.base.adapter.mackTestListData
import com.base.common.util.imageload.imgUrl
import com.base.common.util.imageload.loadImg
import com.baseapp.R

class CircleAdapter : BaseRecycleAdapter<String, BaseHolder>() {
    init {
        this.mackTestListData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseHolder(parent, R.layout.circle_layout)

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.itemView.findViewById<ImageView>(R.id.circleImg).loadImg(imgUrl)
    }
}