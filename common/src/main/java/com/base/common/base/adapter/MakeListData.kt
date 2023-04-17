package com.base.common.base.adapter

import com.chad.library.adapter.base.BaseQuickAdapter

fun BaseRecycleAdapter<String, *>.mackTestListData(count: Int = 20) {
    submitList(arrayListOf<String>().apply {
        repeat(count) {
            add("第 $it 个")
        }
    })
}

fun BaseQuickAdapter<String, *>.mackTestListData(count: Int = 20) {
    submitList(arrayListOf<String>().apply {
        repeat(count) {
            add("第 $it 个")
        }
    })
}