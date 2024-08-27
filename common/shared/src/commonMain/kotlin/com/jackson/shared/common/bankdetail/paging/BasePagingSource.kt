package com.jackson.shared.common.bankdetail.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jackson.shared.common.bankdetail.data.model.PaginationItems

abstract class BasePagingSource<Value : Any> : PagingSource<Int, Value>() {

    protected abstract suspend fun fetchData(page: Int, limit: Int): PaginationItems<Value>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        val currentPage = params.key ?: 1
        val limit = params.loadSize
        return try {
            val response = fetchData(currentPage, limit)
            LoadResult.Page(
                    data = response.items,
                    prevKey = if (currentPage == 1) null else currentPage - 1,
                    nextKey =  (currentPage + 1).takeIf { response.items.lastIndex >= currentPage }
                    )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return state.anchorPosition
    }

}