package com.nbh.juiceapp.presentation.home.ui

import androidx.paging.PagingData
import com.google.common.truth.Truth.assertThat
import com.nbh.juiceapp.data.repository.JuiceRepository
import com.nbh.juiceapp.presentation.home.model.JuiceModel
import com.nbh.juiceapp.presentation.home.ui.viewmodel.JuiceListViewModel
import com.nbh.juiceapp.utils.collectItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class JuiceListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var juiceListViewModel: JuiceListViewModel
    private val juiceRepository: JuiceRepository = mock()

    private val expectedJuiceModel = JuiceModel(
        id = "1",
        name = "Melon Juice",
        image = "melon_juice.jpg",
        description = "Fresh melon juice"
    )
    private val pagingData = PagingData.from(
        listOf(expectedJuiceModel)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(juiceRepository.getAllJuices()).thenReturn(flowOf(pagingData))
        whenever(juiceRepository.searchJuices(any())).thenReturn(flowOf(pagingData))
        juiceListViewModel = JuiceListViewModel(juiceRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isLoading is false when setLoaded called`() {
        juiceListViewModel.setLoaded()
        assertFalse(juiceListViewModel.isLoading.value)
    }

    @Test
    fun `displayedJuices returns allJuices when query is blank or less than 4 chars`() = runTest {
        juiceListViewModel.onSearchQueryChange("")
        val paging = juiceListViewModel.displayedJuices.first()
        val items = paging.collectItems()

        assertThat(items).containsExactly(expectedJuiceModel)

        juiceListViewModel.onSearchQueryChange("abc")
        val paging2 = juiceListViewModel.displayedJuices.first()
        val items2 = paging2.collectItems()

        assertThat(items2).containsExactly(expectedJuiceModel)
    }

    @Test
    fun `displayedJuices returns searched juices when query is not blank and more than 4 chars`() = runTest {
        juiceListViewModel.onSearchQueryChange("melon")
        val paging3 = juiceListViewModel.displayedJuices.first()
        val items3 = paging3.collectItems()

        assertThat(items3).containsExactly(expectedJuiceModel)
    }

    @Test
    fun `onSearchQueryChange updates correctly searchQuery with newQuery`() {
        val newQuery = "watermelon"
        juiceListViewModel.onSearchQueryChange(newQuery)

        val searchQuery = juiceListViewModel.searchQuery.value

        assertThat(searchQuery).isEqualTo(newQuery)
    }
}