package com.nbh.juiceapp.data.mapper

import com.google.common.truth.Truth.assertThat
import com.nbh.juiceapp.data.response.JuiceResponse
import org.junit.Test

class JuiceMapperTest {

    @Test
    fun `toJuiceModel maps all fields correctly when not null`() {
        val response = JuiceResponse(
            id = "1",
            name = "Orange Juice",
            image = "orange_juice.jpg",
            description = "Fresh orange juice"
        )

        val model = response.toJuiceModel()

        assertThat(model.id).isEqualTo("1")
        assertThat(model.name).isEqualTo("Orange Juice")
        assertThat(model.image).isEqualTo("orange_juice.jpg")
        assertThat(model.description).isEqualTo("Fresh orange juice")
    }

    @Test
    fun `toJuiceModel uses default values when null`() {
        val response = JuiceResponse(
            id = "2",
            name = "Apple Juice",
            image = null,
            description = null
        )

        val model = response.toJuiceModel()

        assertThat(model.id).isEqualTo("2")
        assertThat(model.name).isEqualTo("Apple Juice")
        assertThat(model.image).isEqualTo("")
        assertThat(model.description).isEqualTo("No description")
    }
}