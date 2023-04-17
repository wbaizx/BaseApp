package com.baseapp.main.mvvm

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import com.base.common.base.fragment.BaseBindModelFragment
import com.base.common.helper.setOnSingleClickListener
import com.baseapp.R
import com.baseapp.databinding.FragmentMvvmDemoFBinding
import com.baseapp.main.mvvm.adapter.MVVMBindAdapter
import com.baseapp.main.mvvm.adapter.MVVMBindBean
import com.baseapp.main.mvvm.adapter.MVVMListAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

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

        val adapter = MVVMBindAdapter()
        binding.recyclerView1.adapter = adapter
        adapter.isEmptyViewEnable = true
        adapter.setEmptyViewLayout(requireContext(), R.layout.list_item_empty)

        binding.refreshLayout1.setOnRefreshListener {
            binding.refreshLayout1.load(adapter)
        }

        binding.refreshLayout1.setOnLoadMoreListener {
            binding.refreshLayout1.load(adapter)
        }
    }

    private fun init2() {
        val adapter = MVVMListAdapter()

        binding.recyclerView2.adapter = adapter
        adapter.isEmptyViewEnable = true
        val empty = LayoutInflater.from(context).inflate(R.layout.list_item_empty, binding.recyclerView2, false)
        empty.setOnSingleClickListener {
            Handler(Looper.getMainLooper()).postDelayed({ adapter.addAll(arrayListOf(MVVMBindBean("5"))) }, 1500)
        }
        adapter.emptyView = empty

        binding.refreshLayout2.setOnRefreshListener {
            binding.refreshLayout2.load(adapter)
        }

        binding.refreshLayout2.setOnLoadMoreListener {
            binding.refreshLayout2.load(adapter)
        }
    }

    private fun SmartRefreshLayout.load(adapter: BaseQuickAdapter<MVVMBindBean, *>) {
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.addAll(arrayListOf(MVVMBindBean("5")))
            finishLoadMore()
            finishRefresh()
        }, 1500)
    }

    override fun onFirstVisible() {
    }

    override fun onVisible() {
    }

    override fun onHide() {
    }
}