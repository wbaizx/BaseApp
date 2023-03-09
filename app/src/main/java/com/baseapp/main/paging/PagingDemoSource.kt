package com.baseapp.main.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.base.common.util.debugLog
import kotlinx.coroutines.delay

class PagingDemoSource(val reps: PagingRepository) : PagingSource<Int, String>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val page = params.key ?: 0
        val pageSize = params.loadSize

        debugLog("PagingDemo", "load start")

        try {
            if (page == 3) {
                throw IllegalArgumentException()
            }

            val data = arrayListOf<String>()
            repeat(pageSize) {
                data.add("$page-$it")
            }

            delay(2000)

            val prevKey = page - 1
            val nextKey = page + 1

            debugLog("PagingDemo", "page $page pageSize $pageSize prevKey $prevKey nextKey $nextKey")
            return LoadResult.Page(data, prevKey, nextKey)

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        val page = state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
        debugLog("PagingDemo", "getRefreshKey $page")
        return page
    }
}