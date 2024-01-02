package com.truta.traveljournal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.repository.MemoryRepository

class DetailsViewModel(private val repository: MemoryRepository, private val memoryId: Int) :
    ViewModel() {
    var memories = repository.allMemories
    var currentMemory: Memory? = null
    var pictureUris: MutableList<String> = mutableListOf()

    fun getMemoryById(id: Int): Memory? {
        return memories.value?.find { it -> it.id == id }
    }

}

class DetailsModelFactory(private val repository: MemoryRepository, private val memoryId: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) return DetailsViewModel(
            repository,
            memoryId
        ) as T
        throw IllegalAccessException("Unknown class for ViewModel")
    }
}