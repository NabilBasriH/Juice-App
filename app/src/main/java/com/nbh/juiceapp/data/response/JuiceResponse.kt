package com.nbh.juiceapp.data.response

import com.google.gson.annotations.SerializedName

data class JuiceResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("product_name") val name: String,
    @SerializedName("image_url") val image: String?,
    @SerializedName("ingredients_text_en") val description: String?
)
