package com.nbh.juiceapp.data.remote

import com.google.common.truth.Truth.assertThat
import com.nbh.juiceapp.utils.readJsonFromResources
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JuiceApiServiceTest {

    private lateinit var juiceApiService: JuiceApiService

    @Before
    fun setup() {
        val json = readJsonFromResources("api-response/ResultResponse.json")

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val response = Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(
                        json.toResponseBody("application/json".toMediaTypeOrNull())
                    )
                    .addHeader("content-type", "application/json")
                    .build()
                response
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://test-api")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        juiceApiService = retrofit.create(JuiceApiService::class.java)
    }

    @Test
    fun getJuicesReturnsParsedResultResponse() = runTest {
        val response = juiceApiService.getJuices(page = 1, pageSize = 20)

        assertThat(response.count).isEqualTo(2)
        assertThat(response.products[0].name).isEqualTo("Orange Juice")
        assertThat(response.products[1].id).isEqualTo("456")
    }

    @Test
    fun searchJuiceReturnsParsedResultResponse() = runTest {
        val response = juiceApiService.searchJuice(page = 1, pageSize = 20, query = "Juice")

        assertThat(response.count).isEqualTo(2)
        assertThat(response.products[0].image).isEqualTo("orange_juice.jpg")
        assertThat(response.products[1].description).isEqualTo("Fresh apple juice")
    }
}