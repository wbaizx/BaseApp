package com.baseapp.main.shape_btn

import com.base.common.base.BaseActivity
import com.base.common.util.log
import com.baseapp.R
import kotlinx.android.synthetic.main.activity_show_shapebtn.*

class ShowShapeBtnActivity : BaseActivity() {
    private val TAG = "ShowShapeBtnActivity"

    override fun getContentView() = R.layout.activity_show_shapebtn

    override fun initView() {
        shapeButton.setOnClickListener {
            log(TAG, "shapeButton OnClick")
        }

        shapeDrawableButton.setOnClickListener {
            log(TAG, "shapeDrawableButton OnClick")
        }

        commonButton.setOnClickListener {
            log(TAG, "commonButton OnClick")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log(TAG, "onDestroy")
    }
}