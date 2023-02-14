package com.baseapp.main.item_animation.item_animation1

import androidx.recyclerview.widget.LinearLayoutManager
import com.base.common.base.activity.BaseBindContentActivity
import com.baseapp.R
import com.baseapp.databinding.ActivityItemAnimation1Binding
import jp.wasabeef.recyclerview.animators.ScaleInAnimator

class ItemAnimation1Activity : BaseBindContentActivity<ActivityItemAnimation1Binding>() {
    override fun getContentView() = R.layout.activity_item_animation1

    override fun viewBind(binding: ActivityItemAnimation1Binding) {}

    override fun initView() {
        val adapter = ItemAnimation1Adapter()

        binding.change.setOnClickListener {
            adapter.data.add(5, "测试")
            adapter.notifyItemInserted(5)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //用于item进场动画，
        //setFirstOnly 设置是否只有首次加载
//        recyclerView.adapter = ScaleInAnimationAdapter(adapter).apply {
//            setDuration(2000)
//            setFirstOnly(false)}

        //如果自定义item进场动画可继承 AnimationAdapter（由于构造方法泛型有问题，如果要继承目前只能用java）
        //或者直接在自己的adapter中添加，首次加载通过变量控制(参考AnimationAdapter)
        binding.recyclerView.adapter = TestAnimationAdapter(adapter).apply {
            setDuration(2000)
            setFirstOnly(false)
        }

        //用于item操作动画，用于add,remove,change等操作
        //如果需要修改change动画，可以继承BaseItemAnimator 或者 DefaultItemAnimator
        //仿照 DefaultItemAnimator 重写 animateChange 或者 animateChangeImpl
        binding.recyclerView.itemAnimator = ScaleInAnimator().apply {
            addDuration = 2000
        }
    }
}
