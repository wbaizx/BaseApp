package com.baseapp.main.paging

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.base.common.base.mvvm.BaseMVVMViewModel

class PagingViewModel(private val reps: PagingRepository) : BaseMVVMViewModel() {
    val flow = Pager(PagingConfig(pageSize = 20, prefetchDistance = 1, initialLoadSize = 20)) {
        PagingDemoSource(reps)
    }.flow.cachedIn(viewModelScope)
}