package com.baseapp.main.paging

import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.base.common.base.activity.BaseBindModelActivity
import com.base.common.util.debugLog
import com.baseapp.R
import com.baseapp.databinding.ActivityPagingDemoBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PagingActivity : BaseBindModelActivity<PagingViewModel, ActivityPagingDemoBinding>() {
    override val vm by viewModel<PagingViewModel>()

    override fun getContentView() = R.layout.activity_paging_demo

    override fun viewBind(binding: ActivityPagingDemoBinding) {
        binding.vm = vm
    }

    override fun initView() {
        val pagingAdapter = PagingDemoAdapter()

        lifecycleScope.launch {
            vm.flow.collectLatest {
                debugLog("PagingDemo", "submitData")
                pagingAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        debugLog("PagingDemo", "Flow refresh Loading")
                    }
                    is LoadState.Error -> {
                        debugLog("PagingDemo", "Flow refresh Error")
                    }
                    else -> {
                        debugLog("PagingDemo", "Flow refresh NotLoading")
                    }
                }
                when (loadStates.append) {
                    is LoadState.Loading -> {
                        debugLog("PagingDemo", "Flow append Loading")
                    }
                    is LoadState.Error -> {
                        debugLog("PagingDemo", "Flow append Error")
                    }
                    else -> {
                        debugLog("PagingDemo", "Flow append  NotLoading")
                    }
                }
                when (loadStates.prepend) {
                    is LoadState.Loading -> {
                        debugLog("PagingDemo", "Flow prepend Loading")
                    }
                    is LoadState.Error -> {
                        debugLog("PagingDemo", "Flow prepend Error")
                    }
                    else -> {
                        debugLog("PagingDemo", "Flow prepend  NotLoading")
                    }
                }
            }
        }

        binding.recyclerView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(PagingFooterAdapter {
            debugLog("PagingDemo", "retry")
        }, PagingFooterAdapter {
            debugLog("PagingDemo", "retry")
        })

        binding.retry.setOnClickListener {
            pagingAdapter.refresh()
//            pagingAdapter.retry()
        }
    }

    override fun initObserve() {
    }
}