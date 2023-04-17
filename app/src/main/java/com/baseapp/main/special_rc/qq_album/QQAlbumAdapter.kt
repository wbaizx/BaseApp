package com.baseapp.main.special_rc.qq_album

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.base.common.base.adapter.BaseHolder
import com.base.common.base.adapter.mackTestListData
import com.baseapp.R
import com.chad.library.adapter.base.BaseMultiItemAdapter

private const val IMG = 1
private const val TEXT = 2

class QQAlbumAdapter : BaseMultiItemAdapter<String>() {
    private class ImgHolder(parent: ViewGroup, @LayoutRes id: Int) : BaseHolder(parent, id)
    private class TextHolder(parent: ViewGroup, @LayoutRes id: Int) : BaseHolder(parent, id)


    init {
        this.mackTestListData(41)

        addItemType(TEXT, object : OnMultiItemAdapterListener<String, TextHolder> {
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int) = TextHolder(parent, R.layout.multi_text_layout)

            override fun onBind(holder: TextHolder, position: Int, item: String?) {
            }
        })

        addItemType(IMG, object : OnMultiItemAdapterListener<String, ImgHolder> {
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int) = ImgHolder(parent, R.layout.multi_image_layout)

            override fun onBind(holder: ImgHolder, position: Int, item: String?) {
            }
        })

        onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            if (position == 0 || position == 4 || position == 12 || position == 17
                || position == 22 || position == 28 || position == 36 || position == 39
            ) {
                TEXT
            } else {
                IMG
            }
        }
    }

    override fun isFullSpanItem(itemType: Int): Boolean {
        return if (itemType == TEXT) true else super.isFullSpanItem(itemType)
    }
}