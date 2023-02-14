package com.baseapp.main.special_rc.qq_album

import androidx.recyclerview.widget.GridLayoutManager
import com.base.common.base.activity.BaseBindContentActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityQqalbumBinding

/**
 * 使用 多布局 + setSpanSizeLookup 方式
 * 也可以使用嵌套布局方式
 * 或者分割线方式
 */
class QQAlbumActivity : BaseBindContentActivity<ActivityQqalbumBinding>() {

    override fun getContentView() = R.layout.activity_qqalbum

    override fun viewBind(binding: ActivityQqalbumBinding) {}

    override fun initView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        binding.recyclerView.adapter = QQAlbumAdapter()
    }
}
