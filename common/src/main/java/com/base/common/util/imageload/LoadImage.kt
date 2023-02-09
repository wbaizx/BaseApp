package com.base.common.util.imageload

import android.widget.ImageView

const val imgUrl =
    "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fc1cb0016ce599fff96fcaacd3e01452cffc3c8c1a066c-3qtAwO_fw236&refer=http%3A%2F%2Fhbimg.b0.upaiyun.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1619171623&t=9a878cb48b953a4f36f6382283739fbb"

private val coilEngine: LoadEngine by lazy { CoilEngine() }

private var defaultEngine = coilEngine

fun ImageView.loadImg(any: Any) {
    defaultEngine.loadImg(any, this)
}

fun ImageView.loadBlurImg(any: Any) {
    defaultEngine.loadBlurImg(any, this)
}

fun ImageView.loadCircleImg(any: Any) {
    defaultEngine.loadCircleImg(any, this)
}

fun ImageView.loadRoundImg(any: Any) {
    defaultEngine.loadRoundImg(any, this)
}

