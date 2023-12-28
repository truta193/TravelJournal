package com.truta.traveljournal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.truta.traveljournal.model.Memory

class HomeViewModel: ViewModel() {
    private val _itemList = MutableLiveData<List<Memory>>()
    val itemList: LiveData<List<Memory>> get() = _itemList

    init {
        _itemList.value = listOf(
            Memory("Place 1", "Item 1", false),
            Memory("Place 2", "Item 2", false),
            Memory("Place 3", "Item 2", false),
        )
    }

    fun onFavoriteButtonPress(memory: Memory) {
        val index = _itemList.value?.indexOf(memory)

        if (index != null && index != -1) {
            _itemList.value?.get(index)?.isFavorite = !memory.isFavorite

            _itemList.value = _itemList.value
            Log.d("Buttonpress", "${memory.isFavorite}")
        }

    }

    fun getFavoriteButtonColor(memory: Memory): Int {
        if (memory.isFavorite)
            return androidx.appcompat.R.color.material_grey_50
        return androidx.appcompat.R.color.abc_tint_btn_checkable
    }

}