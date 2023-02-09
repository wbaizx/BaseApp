package com.baseapp.main.special_rc.circle_rc

import android.widget.ImageView
import com.base.common.base.adapter.BaseListAdapter
import com.base.common.util.imageload.imgUrl
import com.base.common.util.imageload.loadImg
import com.baseapp.R
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class CircleAdapter : BaseListAdapter<String, BaseViewHolder>(R.layout.circle_layout) {
    init {
        repeat(15) {
            data.add("啦啦")
        }
    }

    override fun convertUI(holder: BaseViewHolder, item: String) {
        holder.getView<ImageView>(R.id.circleImg).loadImg(imgUrl)
    }
}