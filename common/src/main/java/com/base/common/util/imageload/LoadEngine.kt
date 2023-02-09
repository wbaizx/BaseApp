package com.base.common.util.imageload

import android.widget.ImageView

interface LoadEngine {
    fun loadImg(any: Any, img: ImageView)

    fun loadBlurImg(any: Any, img: ImageView)

    fun loadCircleImg(any: Any, img: ImageView)

    fun loadRoundImg(any: Any, img: ImageView)
}