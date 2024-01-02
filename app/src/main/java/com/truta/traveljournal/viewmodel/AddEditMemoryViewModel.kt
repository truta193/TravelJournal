package com.truta.traveljournal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.Marker
import com.truta.traveljournal.model.Memory
import com.truta.traveljournal.repository.MemoryRepository
import kotlinx.coroutines.launch

class AddMemoryViewModel(private val repository: MemoryRepository) : ViewModel() {
    var memories: LiveData<List<Memory>> = repository.allMemories
    var marker: Marker? = null

    fun upsertMemory(memory: Memory) = viewModelScope.launch {
        repository.upsertMemory(memory)
    }

    fun getMemoryById(id: Int): Memory {
        return memories.value!!.find { it -> it.id == id}!!
    }

}

class AddMemoryModelFactory(private val repository: MemoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddMemoryViewModel::class.java))
            return AddMemoryViewModel(repository) as T
        throw IllegalAccessException("Unknown class for ViewModel")
    }
}