package com.baseapp.main.special_rc.circle_rc

import androidx.recyclerview.widget.LinearLayoutManager
import com.base.common.base.BaseActivity
import com.baseapp.R
import kotlinx.android.synthetic.main.activity_circle_rc.*

class CircleRCActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_circle_rc

    override fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CircleAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(CircleDecoration(adapter))
    }

    override fun initData() {
    }
}
