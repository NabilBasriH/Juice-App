package com.nbh.juiceapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.nbh.juiceapp.data.JuiceApiService
import com.nbh.juiceapp.data.mapper.toJuiceModel
import com.nbh.juiceapp.data.paging.JuicePagingSource
import com.nbh.juiceapp.data.paging.SearchJuicePagingSource
import com.nbh.juiceapp.presentation.home.model.JuiceModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class JuiceRepository @Inject constructor(private val api: JuiceApiService) {

    companion object {
        const val MAX_ITEMS = 10
        const val PREFETCH_ITEMS = 8
    }

    fun getAllJuices(): Flow<PagingData<JuiceModel>> {
        return Pager(
            config = PagingConfig(pageSize = MAX_ITEMS, prefetchDistance = PREFETCH_ITEMS),
            pagingSourceFactory = {
                JuicePagingSource(api)
            }).flow
            .map { pagingData ->
                pagingData.map { it.toJuiceModel() }
            }
    }

    fun searchJuices(query: String): Flow<PagingData<JuiceModel>> {
        return Pager(
            config = PagingConfig(pageSize = MAX_ITEMS, prefetchDistance = PREFETCH_ITEMS),
            pagingSourceFactory = {
                SearchJuicePagingSource(api, query)
            }).flow
            .map { pagingData ->
                pagingData.map { it.toJuiceModel() }
            }
    }
}