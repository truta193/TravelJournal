package com.truta.traveljournal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.repository.MemoryRepository
import com.truta.traveljournal.service.WeatherService
import com.truta.traveljournal.service.WeatherService.Companion.weatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.math.roundToInt

class DetailsViewModel(private val repository: MemoryRepository, weatherService: WeatherService) :
    ViewModel() {
    var memories = repository.allMemories
    var currentMemory: Memory? = null
    var pictureUris: MutableList<String> = mutableListOf()

    private var _weatherGetSuccess = MutableLiveData<Boolean>()
    val weatherGetSuccess: LiveData<Boolean> get() = _weatherGetSuccess

    private var _weatherGetFailure = MutableLiveData<Boolean>()
    val weatherGetFailure: LiveData<Boolean> get() = _weatherGetFailure

    private var _temp = MutableLiveData<String>()
    val temp: LiveData<String> get() = _temp

    private var _icon = MutableLiveData<String>()
    val icon: LiveData<String> get() = _icon

    private var _details = MutableLiveData<String>()
    val details: LiveData<String> get() = _details

    fun getWeatherData(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val isWeather = withContext(Dispatchers.IO) {
                    weatherService.getWeatherData(lat.toString(), lon.toString(), apiKey)
                }
                if (isWeather.isSuccessful) {
                    val weatherData = isWeather.body()
                    _temp.value = weatherData?.main?.temp?.roundToInt().toString() + "°C"
                    _icon.value = WeatherService.BASE_IMAGE_URL + weatherData?.weather?.get(0)?.icon + "@4x.png"
                    _details.value = weatherData?.weather?.get(0)?.main ?: ""
                    _weatherGetSuccess.value = true
                }
            } catch (e: Exception) {
                _weatherGetFailure.value = true
            }
        }
    }

    fun eventWeatherGetFailureFinish() {
        _weatherGetFailure.value = false
    }

    fun getMemoryById(id: Int): Memory? {
        return memories.value?.find { it -> it.id == id }
    }

}

class DetailsModelFactory(
    private val repository: MemoryRepository, weatherService: WeatherService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) return DetailsViewModel(
            repository, weatherService
        ) as T
        throw IllegalAccessException("Unknown class for ViewModel")
    }
}