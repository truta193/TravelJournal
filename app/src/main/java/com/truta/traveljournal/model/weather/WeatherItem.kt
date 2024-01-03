package com.truta.traveljournal.model.weather

import com.squareup.moshi.Json

data class WeatherItem(
    @Json(name = "icon")
    val icon: String = "",
    @Json(name = "description")
    val description: String = "",
    @Json(name = "main")
    val main: String = "",
    @Json(name = "id")
    val id: Int = 0
)