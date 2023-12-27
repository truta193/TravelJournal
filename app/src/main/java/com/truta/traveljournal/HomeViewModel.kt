package com.truta.traveljournal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {
    private val _itemList = MutableLiveData<List<Memory>>()
    val itemList: LiveData<List<Memory>> get() = _itemList

    init {
        _itemList.value = listOf(
            Memory("Place 1", "Item 1"),
            Memory("Place 2", "Item 2"),
            Memory("Place 3", "Item 2"),
        )
    }
}