package com.truta.traveljournal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.truta.traveljournal.repository.MemoryRepository
import com.truta.traveljournal.model.Memory
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MemoryRepository): ViewModel() {
    var memories: LiveData<List<Memory>> = repository.allMemories

    fun upsertMemory(memory: Memory) = viewModelScope.launch {
        repository.upsertMemory(memory)
    }

    fun onFavoriteButtonPress(memory: Memory) = viewModelScope.launch {
        memory.isFavorite = !memory.isFavorite

        repository.upsertMemory(memory)
    }
//
//    fun getFavoriteButtonColor(memory: Memory): Int {
//        if (memory.isFavorite)
//            return androidx.appcompat.R.color.material_grey_50
//        return androidx.appcompat.R.color.abc_tint_btn_checkable
//    }

}

class MemoryModelFactory(private val repository: MemoryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            return HomeViewModel(repository) as T
        throw IllegalAccessException("Unknown class for ViewModel")
    }
}