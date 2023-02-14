package com.baseapp.main.item_animation.item_animation3

import android.animation.AnimatorListenerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.common.base.activity.BaseBindContentActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityItemAnimation3Binding

class ItemAnimation3Activity : BaseBindContentActivity<ActivityItemAnimation3Binding>() {
    private var isLine = true
    private var duration = 500L

    override fun getContentView() = R.layout.activity_item_animation3

    override fun viewBind(binding: ActivityItemAnimation3Binding) {}

    override fun initView() {
        binding.recyclerViewV.post { binding.recyclerViewV.translationX = -binding.layout.width.toFloat() }

        binding.recyclerViewH.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewH.adapter = ItemAnimation3HAdapter()

        binding.recyclerViewV.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerViewV.adapter = ItemAnimation3VAdapter()

        binding.change.setOnClickListener {
            if (isLine) {
                for (index in 0 until binding.recyclerViewV.childCount) {
                    val view = binding.recyclerViewV.getChildAt(index)
                    view.pivotX = view.width.toFloat()
                    view.pivotY = view.height * 5f
                    view.rotation = (340..350).random().toFloat()
                    view.animate().setDuration(duration + (400..600).random()).rotation(360f).start()
                }

                binding.recyclerViewV.animate()
                    .setDuration(duration)
                    .translationX(0f)
                    .setListener(object : AnimatorListenerAdapter() {
                    })
                    .start()

                binding.recyclerViewH.animate()
                    .setDuration(duration)
                    .translationX(binding.layout.width.toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                    })
                    .start()
            } else {
                binding.recyclerViewV.animate()
                    .setDuration(duration)
                    .translationX(-binding.layout.width.toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                    })
                    .start()

                binding.recyclerViewH.animate()
                    .setDuration(duration)
                    .translationX(0f)
                    .setListener(object : AnimatorListenerAdapter() {
                    })
                    .start()
            }
            isLine = !isLine
        }
    }
}
