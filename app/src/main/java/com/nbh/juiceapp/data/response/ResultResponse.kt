package com.nbh.juiceapp.data.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("page_count") val pageCount: Int,
    @SerializedName("page_size") val pageSize: Int,
    @SerializedName("products") val products: List<JuiceResponse>,
    @SerializedName("skip") val skip: Int,
)
