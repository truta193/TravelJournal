package com.truta.traveljournal.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.truta.traveljournal.model.weather.WeatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather?units=metric")
    suspend fun getWeatherData(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String
    ) : Response<WeatherResponse>

    companion object{
        private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        val BASE_IMAGE_URL = "https://openweathermap.org/img/wn/"
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        private fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(BASE_URL)
                .build()
        }
        val weatherService: WeatherService by lazy {
            getRetrofitInstance().create(WeatherService::class.java)
        }
    }

}