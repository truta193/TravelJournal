package com.truta.traveljournal.model.weather

import com.squareup.moshi.Json

data class WeatherResponse(
    @Json(name = "main")
    val main: Main,
    @Json(name = "weather")
    val weather: List<WeatherItem>?
)