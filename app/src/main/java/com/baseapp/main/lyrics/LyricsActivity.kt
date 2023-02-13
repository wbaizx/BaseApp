package com.baseapp.main.lyrics

import com.base.common.base.BaseActivity
import com.baseapp.R
import kotlinx.android.synthetic.main.activity_show_lyrics.*

class LyricsActivity : BaseActivity(){

    override fun getContentView()= R.layout.activity_show_lyrics

    override fun initView() {
        readToolTextListView.updateLayout()
        readToolTextListView.startProgress()
    }
}