package com.nbh.juiceapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nbh.juiceapp.data.remote.JuiceApiService
import com.nbh.juiceapp.data.response.JuiceResponse
import javax.inject.Inject

class JuicePagingSource @Inject constructor(private val api: JuiceApiService) :
    PagingSource<Int, JuiceResponse>() {
    override fun getRefreshKey(state: PagingState<Int, JuiceResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JuiceResponse> {
        val page = params.key ?: 1

        return try {
            val response = api.getJuices(page = page, pageSize = params.loadSize)
            val juices = response.products

            val pageCount = response.pageCount

            LoadResult.Page(
                data = juices,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= pageCount) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}