package com.baseapp.main.mvvm

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.activityViewModels
import com.base.common.base.fragment.BaseBindModelFragment
import com.baseapp.R
import com.baseapp.databinding.FragmentMvvmDemoFBinding
import com.baseapp.main.mvvm.adapter.MVVMBindAdapter
import com.baseapp.main.mvvm.adapter.MVVMListAdapter
import com.baseapp.main.mvvm.adapter.MVVMBindBean

/**
 * 测试 mvvm 下的 adapter
 */
class MVVMDemoFragment : BaseBindModelFragment<MVVMDemoViewModel, FragmentMvvmDemoFBinding>() {

    /**
     * viewModel 使用koin注入方式
     */
    override val vm by activityViewModels<MVVMDemoViewModel>()

    override fun initObserve() {
    }

    override fun getContentView() = R.layout.fragment_mvvm_demo_f

    override fun viewBind(binding: FragmentMvvmDemoFBinding) {
        binding.vm = vm
    }

    override fun createView(view: View) {
        init1()
        init2()
    }

    private fun init1() {
        //xml中设置了头部脚部
//        refreshLayout1.setRefreshHeader(ClassicsHeader(context))
//        refreshLayout1.setRefreshFooter(ClassicsFooter(context))

        binding.recyclerView1.adapter = MVVMBindAdapter().apply {

            setDefaultEmptyView(this@MVVMDemoFragment.context, binding.recyclerView1)

            setRefreshAndLoadMore(binding.refreshLayout1) {
                Handler(Looper.getMainLooper()).postDelayed({ addPageData(arrayListOf(MVVMBindBean("5"))) }, 1500)
            }
        }
    }

    private fun init2() {
        val adapter = MVVMListAdapter()

        binding.recyclerView2.adapter = adapter

        adapter.setDefaultEmptyView(emptyClick = {
            Handler(Looper.getMainLooper()).postDelayed({ adapter.addPageData(arrayListOf(MVVMBindBean("5"))) }, 1500)
        })

        adapter.setRefreshAndLoadMore(binding.refreshLayout2) {
            Handler(Looper.getMainLooper()).postDelayed({ adapter.addPageData(null) }, 1500)
        }
    }


    override fun onFirstVisible() {
    }

    override fun onVisible() {
    }

    override fun onHide() {
    }
}