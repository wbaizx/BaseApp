package com.baseapp.main.shape_btn

import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.util.debugLog
import com.baseapp.R
import com.baseapp.databinding.ActivityShowShapebtnBinding

private const val TAG = "ShowShapeBtnActivity"

class ShowShapeBtnActivity : BaseBindContentActivity<ActivityShowShapebtnBinding>() {

    override fun getContentView() = R.layout.activity_show_shapebtn

    override fun viewBind(binding: ActivityShowShapebtnBinding) {
    }

    override fun initView() {
        binding.shapeButton.setOnClickListener {
            debugLog(TAG, "shapeButton OnClick")
        }

        binding.shapeDrawableButton.setOnClickListener {
            debugLog(TAG, "shapeDrawableButton OnClick")
        }

        binding.commonButton.setOnClickListener {
            debugLog(TAG, "commonButton OnClick")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        debugLog(TAG, "onDestroy")
    }
}