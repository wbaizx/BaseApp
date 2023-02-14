package com.baseapp.main.shape_btn

import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.util.log
import com.baseapp.R
import com.baseapp.databinding.ActivityShowShapebtnBinding

private const val TAG = "ShowShapeBtnActivity"

class ShowShapeBtnActivity : BaseBindContentActivity<ActivityShowShapebtnBinding>() {

    override fun getContentView() = R.layout.activity_show_shapebtn

    override fun viewBind(binding: ActivityShowShapebtnBinding) {
    }

    override fun initView() {
        binding.shapeButton.setOnClickListener {
            log(TAG, "shapeButton OnClick")
        }

        binding.shapeDrawableButton.setOnClickListener {
            log(TAG, "shapeDrawableButton OnClick")
        }

        binding.commonButton.setOnClickListener {
            log(TAG, "commonButton OnClick")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log(TAG, "onDestroy")
    }
}