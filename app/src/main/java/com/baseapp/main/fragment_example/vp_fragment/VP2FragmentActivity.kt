package com.baseapp.main.fragment_example.vp_fragment

import androidx.viewpager2.widget.ViewPager2
import com.base.common.base.activity.BaseBindContentActivity
import com.base.common.base.adapter.BaseViewPagerAdapter
import com.base.common.util.log
import com.baseapp.R
import com.baseapp.databinding.ActivityVpfragmentBinding
import com.baseapp.main.fragment_example.fm.TestFragment

private const val TAG = "VP2FragmentActivity"

class VP2FragmentActivity : BaseBindContentActivity<ActivityVpfragmentBinding>() {

    override fun getContentView() = R.layout.activity_vpfragment

    override fun viewBind(binding: ActivityVpfragmentBinding) {}

    override fun initView() {
        binding.viewPager2.offscreenPageLimit = 1
        binding.viewPager2.adapter = BaseViewPagerAdapter(
            this, arrayListOf(
                TestFragment("1"),
                TestFragment("2"),
                TestFragment("3"),
                TestFragment("4"),
                TestFragment("5"),
                TestFragment("6"),
                TestFragment("7"),
            )
        )

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                log(TAG, "$position -- $positionOffset -- $positionOffsetPixels")
            }

            //用这个
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.simpleTabLayout.setPosition(position)
                log(TAG, "onPageSelected $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        binding.simpleTabLayout.setListener {
            binding.viewPager2.setCurrentItem(it, false)
        }

        binding.simpleTabLayout.setData(arrayListOf("第1个", "第2个", "第3个", "第4个", "第5个", "第6个", "第7个"))
    }
}
