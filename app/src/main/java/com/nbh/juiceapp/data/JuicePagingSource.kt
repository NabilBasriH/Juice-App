package com.nbh.juiceapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nbh.juiceapp.data.mapper.toJuiceModel
import com.nbh.juiceapp.presentation.home.model.JuiceModel
import javax.inject.Inject

class JuicePagingSource @Inject constructor(private val api: JuiceApiService) :
    PagingSource<Int, JuiceModel>() {
    override fun getRefreshKey(state: PagingState<Int, JuiceModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, JuiceModel> {
        val page = params.key ?: 1

        return try {
            val response = api.getJuices(page = page, pageSize = params.loadSize)
            val juices = response.products.map { it.toJuiceModel() }

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