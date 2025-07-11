package com.nbh.juiceapp.data.remote

import com.nbh.juiceapp.data.response.ResultResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface JuiceApiService {
    
    @GET("/category/juices.json")
    suspend fun getJuices(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): ResultResponse

    @GET("/cgi/search.pl")
    suspend fun searchJuice(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("search_terms") query: String,
        @Query("tagtype_0") tagType: String = "categories",
        @Query("tag_contains_0") tagContains: String = "contains",
        @Query("tag_0") tag: String = "juices",
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1
    ): ResultResponse
}