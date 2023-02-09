package com.baseapp.main.special_rc.indicator

import android.widget.ImageView
import com.base.common.base.adapter.BaseListAdapter
import com.base.common.util.imageload.imgUrl
import com.base.common.util.imageload.loadBlurImg
import com.baseapp.R
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class IndicatorAdapter : BaseListAdapter<String, BaseViewHolder>(R.layout.item_indicator_item) {
    init {
        repeat(20) {
            data.add("啦啦")
        }
    }

    override fun convertUI(holder: BaseViewHolder, item: String) {
        (holder.itemView as ImageView).loadBlurImg(imgUrl)
    }
}