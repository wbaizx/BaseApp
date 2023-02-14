package com.baseapp.main.special_rc.connection_rc

import androidx.recyclerview.widget.LinearLayoutManager
import com.base.common.base.activity.BaseBindContentActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityConnectionRcBinding

class ConnectionRCActivity : BaseBindContentActivity<ActivityConnectionRcBinding>() {

    override fun getContentView() = R.layout.activity_connection_rc

    override fun viewBind(binding: ActivityConnectionRcBinding) {}

    override fun initView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ConnectionAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(ConnectionDecoration(adapter))

    }
}
