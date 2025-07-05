package com.nbh.juiceapp.presentation.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nbh.juiceapp.data.JuiceRepository
import com.nbh.juiceapp.presentation.home.model.JuiceModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class JuiceListViewModel @Inject constructor(juiceRepository: JuiceRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val allJuices: Flow<PagingData<JuiceModel>> =
        juiceRepository.getAllJuices()
            .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val displayedJuices: Flow<PagingData<JuiceModel>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank() || query.length < 4) {
                allJuices
            } else {
                juiceRepository.searchJuices(query)
            }
        }.cachedIn(viewModelScope)

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}