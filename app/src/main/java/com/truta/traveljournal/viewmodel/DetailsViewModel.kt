package com.truta.traveljournal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.repository.MemoryRepository

class DetailsViewModel(private val repository: MemoryRepository) : ViewModel() {
    var memories: LiveData<List<Memory>> = repository.allMemories
    var currentMemory: Memory? = null

    fun getMemoryById(id: Int): Memory? {
        return memories.value?.find { it -> it.id == id }
    }
}

class DetailsModelFactory(private val repository: MemoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java))
            return DetailsViewModel(repository) as T
        throw IllegalAccessException("Unknown class for ViewModel")
    }
}