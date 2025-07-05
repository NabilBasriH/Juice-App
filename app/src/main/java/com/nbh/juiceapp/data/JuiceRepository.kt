package com.nbh.juiceapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.nbh.juiceapp.presentation.home.model.JuiceModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class JuiceRepository @Inject constructor(private val api: JuiceApiService) {

    companion object {
        const val MAX_ITEMS = 10
        const val PREFETCH_ITEMS = 8
    }

    fun getAllJuices(): Flow<PagingData<JuiceModel>> {
        return Pager(config = PagingConfig(pageSize = MAX_ITEMS, prefetchDistance = PREFETCH_ITEMS),
            pagingSourceFactory = {
                JuicePagingSource(api)
            }).flow
    }

    fun searchJuices(query: String): Flow<PagingData<JuiceModel>> {
        return Pager(config = PagingConfig(pageSize = MAX_ITEMS, prefetchDistance = PREFETCH_ITEMS),
            pagingSourceFactory = {
                SearchJuicePagingSource(api, query)
            }).flow
    }
}