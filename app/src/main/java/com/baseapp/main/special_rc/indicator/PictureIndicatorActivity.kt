package com.baseapp.main.special_rc.indicator

import androidx.viewpager2.widget.ViewPager2
import com.base.common.base.activity.BaseBindContentActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityPictureIndicatorBinding

class PictureIndicatorActivity : BaseBindContentActivity<ActivityPictureIndicatorBinding>() {

    override fun getContentView() = R.layout.activity_picture_indicator

    override fun viewBind(binding: ActivityPictureIndicatorBinding) {}

    override fun initView() {
        binding.imgViewPager.offscreenPageLimit = 8
        binding.imgViewPager.adapter = IndicatorAdapter()

        binding.imgViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }
}