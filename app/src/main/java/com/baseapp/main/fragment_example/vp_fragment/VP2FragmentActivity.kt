package com.baseapp.main.fragment_example.vp_fragment

import androidx.viewpager2.widget.ViewPager2
import com.base.common.base.BaseActivity
import com.base.common.base.adapter.BaseViewPagerAdapter
import com.base.common.util.log
import com.baseapp.R
import com.baseapp.main.fragment_example.fm.TestFragment
import kotlinx.android.synthetic.main.activity_vpfragment.*

class VP2FragmentActivity : BaseActivity() {
    private val TAG = "VP2FragmentActivity"

    override fun getContentView() = R.layout.activity_vpfragment

    override fun initView() {
        viewPager2.offscreenPageLimit = 1
        viewPager2.adapter = BaseViewPagerAdapter(
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

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                log(TAG, "$position -- $positionOffset -- $positionOffsetPixels")
            }

            //用这个
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                simpleTabLayout.setPosition(position)
                log(TAG, "onPageSelected $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })

        simpleTabLayout.setListener {
            viewPager2.setCurrentItem(it, false)
        }

        simpleTabLayout.setData(arrayListOf("第1个", "第2个", "第3个", "第4个", "第5个", "第6个", "第7个"))
    }
}
