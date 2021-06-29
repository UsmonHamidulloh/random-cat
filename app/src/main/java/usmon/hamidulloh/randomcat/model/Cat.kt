package usmon.hamidulloh.randomcat.model

import com.squareup.moshi.Json

data class Cat(
        @field:Json(name = "id")
        val id: String,

        @field:Json(name = "url")
        val url: String,

        @field:Json(name = "width")
        val width: Int,

        @field:Json(name = "height")
        val height: Int
)