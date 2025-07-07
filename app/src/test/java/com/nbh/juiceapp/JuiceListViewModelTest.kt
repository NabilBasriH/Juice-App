package com.nbh.juiceapp

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.nbh.juiceapp.data.JuiceRepository
import com.nbh.juiceapp.presentation.home.model.JuiceModel
import com.nbh.juiceapp.presentation.home.ui.viewmodel.JuiceListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class JuiceListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var juiceListViewModel: JuiceListViewModel
    private val juiceRepository: JuiceRepository = mock()

    private val pagingData = PagingData.from(
        listOf(
            JuiceModel(
                id = "1",
                name = "Melon Juice",
                image = "melon_juice.jpg",
                description = "Fresh melon juice"
            )
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        whenever(juiceRepository.getAllJuices()).thenReturn(flowOf(pagingData))
        juiceListViewModel = JuiceListViewModel(juiceRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `displayedJuices returns allJuices when query is blank or less than 4 chars`() = runTest {
        juiceListViewModel.onSearchQueryChange("")
        juiceListViewModel.displayedJuices.test {
            val emitted = awaitItem()
            assertThat(emitted).isEqualTo(pagingData)
        }

        juiceListViewModel.onSearchQueryChange("abc")
        juiceListViewModel.displayedJuices.test {
            val emitted = awaitItem()
            assertThat(emitted).isEqualTo(pagingData)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `displayedJuices returns searched juices when query is not blank and more than 4 chars`() = runTest {
        val query = "melon"

        whenever(juiceRepository.searchJuices(query)).thenReturn(flowOf(pagingData))

        juiceListViewModel.onSearchQueryChange(query)
        juiceListViewModel.displayedJuices.test {
            advanceTimeBy(300)

            val emitted = awaitItem()
            assertThat(emitted).isEqualTo(pagingData)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchQueryChange updates correctly searchQuery with newQuery`() {
        val newQuery = "watermelon"
        juiceListViewModel.onSearchQueryChange(newQuery)

        val searchQuery = juiceListViewModel.searchQuery.value

        assertThat(searchQuery).isEqualTo(newQuery)
    }
}