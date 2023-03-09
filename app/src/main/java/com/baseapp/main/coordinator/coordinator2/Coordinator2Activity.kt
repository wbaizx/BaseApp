package com.baseapp.main.coordinator.coordinator2

import androidx.constraintlayout.motion.widget.MotionLayout
import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.base.adapter.BaseListAdapter
import com.base.common.util.debugLog
import com.baseapp.R
import com.baseapp.databinding.ActivityCoordinator2Binding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 目前为止，绝大部分动画通过 View.animate() 或者 MotionLayout都能完成了
 */

private const val TAG = "Coordinator2Activity"

class Coordinator2Activity : BaseBindContentActivity<ActivityCoordinator2Binding>() {

    override fun getContentView() = R.layout.activity_coordinator2

    override fun viewBind(binding: ActivityCoordinator2Binding) {}

    override fun initView() {
        binding.recyclerView.adapter = object : BaseListAdapter<String, BaseViewHolder>(R.layout.item_default_layout) {
            override fun convertUI(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.item_text, "$item  ${holder.bindingAdapterPosition - headerLayoutCount}")
            }
        }.apply { setNewInstance(arrayListOf("啦啦", "啦啦", "啦啦", "啦啦", "啦啦", "啦啦", "啦啦", "啦啦", "啦啦", "啦啦", "啦啦", "啦啦", "啦啦")) }

        binding.MotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                debugLog(TAG, "onTransitionStarted")
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                debugLog(TAG, "onTransitionChange  $p3")
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                debugLog(TAG, "onTransitionCompleted")
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                debugLog(TAG, "onTransitionTrigger")
            }
        })
    }
}