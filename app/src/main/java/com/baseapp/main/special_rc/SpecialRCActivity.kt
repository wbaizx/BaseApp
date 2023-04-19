package com.baseapp.main.special_rc

import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.util.launchActivity
import com.baseapp.R
import com.baseapp.databinding.ActivitySpecialRcBinding
import com.baseapp.main.special_rc.circle_rc.CircleRCActivity
import com.baseapp.main.special_rc.connection_rc.ConnectionRCActivity
import com.baseapp.main.special_rc.damping_rc.DampingRCActivity
import com.baseapp.main.special_rc.gallery.GalleryActivity
import com.baseapp.main.special_rc.indicator.PictureIndicatorActivity
import com.baseapp.main.special_rc.qq_album.QQAlbumActivity
import com.baseapp.main.special_rc.scrollto_rc.ScrollToRCActivity
import com.baseapp.main.special_rc.slide_rc.SlideRCActivity

class SpecialRCActivity : BaseBindContentActivity<ActivitySpecialRcBinding>() {
    override fun getContentView() = R.layout.activity_special_rc

    override fun viewBind(binding: ActivitySpecialRcBinding) {}

    override fun initView() {
        binding.dampingRc.setOnClickListener {
            launchActivity(this, DampingRCActivity::class.java)
        }

        binding.recyclerViewGallery.setOnClickListener {
            launchActivity(this, GalleryActivity::class.java)
        }

        binding.scrollToRecyclerView.setOnClickListener {
            launchActivity(this, ScrollToRCActivity::class.java)
        }

        binding.qqAlbum.setOnClickListener {
            launchActivity(this, QQAlbumActivity::class.java)
        }

        binding.slidRc.setOnClickListener {
            launchActivity(this, SlideRCActivity::class.java)
        }

        binding.connectionRecyclerView.setOnClickListener {
            launchActivity(this, ConnectionRCActivity::class.java)
        }

        binding.overlappingRecyclerView.setOnClickListener {
            launchActivity(this, CircleRCActivity::class.java)
        }

        binding.pictureIndicator.setOnClickListener {
            launchActivity(this, PictureIndicatorActivity::class.java)
        }
    }
}
