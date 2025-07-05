package com.nbh.juiceapp.data.mapper

import com.nbh.juiceapp.data.response.JuiceResponse
import com.nbh.juiceapp.presentation.home.model.JuiceModel

fun JuiceResponse.toJuiceModel(): JuiceModel {
    return JuiceModel(
        id = this.id,
        name = this.name,
        image = this.image ?: "",
        description = this.description ?: "No description"
    )
}