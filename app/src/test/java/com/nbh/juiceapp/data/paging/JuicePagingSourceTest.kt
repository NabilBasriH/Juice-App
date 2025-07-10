package com.nbh.juiceapp.data.paging

import androidx.paging.PagingSource
import com.google.common.truth.Truth.assertThat
import com.nbh.juiceapp.data.JuiceApiService
import com.nbh.juiceapp.utils.readJsonFromResources
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JuicePagingSourceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var juiceApiService: JuiceApiService
    private lateinit var juicePagingSource: JuicePagingSource

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        juiceApiService = retrofit.create(JuiceApiService::class.java)

        juicePagingSource = JuicePagingSource(juiceApiService)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `load returns Page when response is successful`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(readJsonFromResources("api-response/ResultResponse.json"))
            .addHeader("Content-Type", "application/json")
        mockWebServer.enqueue(mockResponse)

        val loadResult = juicePagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertThat(loadResult is PagingSource.LoadResult.Page).isTrue()
        loadResult as PagingSource.LoadResult.Page

        assertThat(loadResult.data.size).isEqualTo(2)
        assertThat(loadResult.data[0].name).isEqualTo("Orange Juice")
        assertThat(loadResult.data[1].id).isEqualTo("456")

        assertThat(loadResult.prevKey).isNull()
        assertThat(loadResult.nextKey).isEqualTo(2)
    }

    @Test
    fun `load returns error when response is error`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))

        val loadResult = juicePagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertThat(loadResult is PagingSource.LoadResult.Error).isTrue()
    }
}