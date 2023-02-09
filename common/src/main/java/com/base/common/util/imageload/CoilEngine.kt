package com.base.common.util.imageload

import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.base.common.R

class CoilEngine : LoadEngine {

    override fun loadImg(any: Any, img: ImageView) {
        img.load(any) {
            placeholder(R.mipmap.placeholder_icon)
            error(R.mipmap.test_icon)
        }
    }

    override fun loadBlurImg(any: Any, img: ImageView) {
        img.load(any) {
            //coil 2.0没有自带高斯模糊
//            transformations(BlurTransformation(img.context, 25f, 3f))
        }
    }

    override fun loadCircleImg(any: Any, img: ImageView) {
        img.load(any) {
            transformations(CircleCropTransformation())
        }
    }

    override fun loadRoundImg(any: Any, img: ImageView) {
        img.load(any) {
            transformations(RoundedCornersTransformation(50f, 50f, 50f, 50f))
        }
    }
}