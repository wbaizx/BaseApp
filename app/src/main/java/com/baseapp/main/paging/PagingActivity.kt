package com.baseapp.main.paging

import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.base.common.base.mvvm.BaseMVVMActivity
import com.base.common.util.log
import com.baseapp.R
import com.baseapp.databinding.ActivityPagingDemoBinding
import kotlinx.android.synthetic.main.activity_paging_demo.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PagingActivity : BaseMVVMActivity<ActivityPagingDemoBinding>() {
    override val viewModel by viewModel<PagingViewModel>()

    override fun getContentView() = R.layout.activity_paging_demo

    override fun bindModelId(binding: ActivityPagingDemoBinding) {
        binding.viewModel = viewModel
    }

    override fun initView() {
        val pagingAdapter = PagingDemoAdapter()

        lifecycleScope.launch {
            viewModel.flow.collectLatest {
                log("PagingDemo", "submitData")
                pagingAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        log("PagingDemo", "Flow refresh Loading")
                    }
                    is LoadState.Error -> {
                        log("PagingDemo", "Flow refresh Error")
                    }
                    else -> {
                        log("PagingDemo", "Flow refresh NotLoading")
                    }
                }
                when (loadStates.append) {
                    is LoadState.Loading -> {
                        log("PagingDemo", "Flow append Loading")
                    }
                    is LoadState.Error -> {
                        log("PagingDemo", "Flow append Error")
                    }
                    else -> {
                        log("PagingDemo", "Flow append  NotLoading")
                    }
                }
                when (loadStates.prepend) {
                    is LoadState.Loading -> {
                        log("PagingDemo", "Flow prepend Loading")
                    }
                    is LoadState.Error -> {
                        log("PagingDemo", "Flow prepend Error")
                    }
                    else -> {
                        log("PagingDemo", "Flow prepend  NotLoading")
                    }
                }
            }
        }

        recyclerView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(PagingFooterAdapter {
            log("PagingDemo", "retry")
        }, PagingFooterAdapter {
            log("PagingDemo", "retry")
        })

        retry.setOnClickListener {
            pagingAdapter.refresh()
//            pagingAdapter.retry()
        }
    }

    override fun initData() {
    }
}