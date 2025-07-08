package com.nbh.juiceapp.data.repository

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.nbh.juiceapp.data.JuiceApiService
import com.nbh.juiceapp.data.JuicePagingSource
import com.nbh.juiceapp.data.SearchJuicePagingSource
import com.nbh.juiceapp.data.response.JuiceResponse
import com.nbh.juiceapp.data.response.ResultResponse
import com.nbh.juiceapp.presentation.home.model.JuiceModel
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class JuiceRepositoryTest {

    private lateinit var juiceRepository: JuiceRepository
    private lateinit var fakeApi: JuiceApiService

    private val PAGE = 1
    private val PAGE_SIZE = 20

    private val juiceResponse = JuiceResponse(
        id = "1",
        name = "Tomato Juice",
        image = "tomato_juice.jpg",
        description = "Fresh tomato juice"
    )

    private val resultResponse = ResultResponse(
        count = 133,
        page = PAGE,
        pageCount = 20,
        pageSize = PAGE_SIZE,
        products = listOf(juiceResponse),
        skip = 0
    )

    private val expectedModel = JuiceModel(
        id = "1",
        name = "Tomato Juice",
        image = "tomato_juice.jpg",
        description = "Fresh tomato juice"
    )

    @Before
    fun setup() {
        fakeApi = mock()
        juiceRepository = JuiceRepository(fakeApi)
    }

    @Test
    fun `getAllJuices return paging juice data`() = runTest {

        whenever(fakeApi.getJuices(PAGE, PAGE_SIZE)).thenReturn(resultResponse)

        val pagingSource = JuicePagingSource(fakeApi)

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = PAGE_SIZE,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(expectedModel),
            prevKey = null,
            nextKey = 2
        )

        assertThat(loadResult).isEqualTo(expected)
    }

    @Test
    fun `searchJuices return paging juices searched`() = runTest {
        val query = "tomato"

        whenever(fakeApi.searchJuice(PAGE, PAGE_SIZE, query)).thenReturn(resultResponse)

        val pagingSource = SearchJuicePagingSource(fakeApi, query)

        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = PAGE_SIZE,
                placeholdersEnabled = false
            )
        )

        val expected = PagingSource.LoadResult.Page(
            data = listOf(expectedModel),
            prevKey = null,
            nextKey = 2
        )

        assertThat(loadResult).isEqualTo(expected)
    }
}