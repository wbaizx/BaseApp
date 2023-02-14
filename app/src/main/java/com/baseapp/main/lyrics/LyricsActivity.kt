package com.baseapp.main.lyrics

import com.base.common.base.activity.BaseBindContentActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityShowLyricsBinding

class LyricsActivity : BaseBindContentActivity<ActivityShowLyricsBinding>() {

    override fun getContentView() = R.layout.activity_show_lyrics

    override fun viewBind(binding: ActivityShowLyricsBinding) {
    }

    override fun initView() {
        binding.readToolTextListView.updateLayout()
        binding.readToolTextListView.startProgress()
    }
}