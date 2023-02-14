package com.baseapp.main.special_rc.gallery

import androidx.recyclerview.widget.LinearLayoutManager
import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.util.log
import com.baseapp.R
import com.baseapp.databinding.ActivityGalleryBinding

class GalleryActivity : BaseBindContentActivity<ActivityGalleryBinding>() {
    private val TAG = "GalleryActivity"

    override fun getContentView() = R.layout.activity_gallery

    override fun viewBind(binding: ActivityGalleryBinding) {}

    override fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = GalleryAdapter()

        GalleryHelper {
            log(TAG, "$it")
        }.attachToRecyclerView(binding.recyclerView)
    }
}
