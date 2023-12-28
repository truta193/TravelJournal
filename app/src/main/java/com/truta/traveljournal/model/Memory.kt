package com.truta.traveljournal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memories")
data class Memory (
    var placeName: String,
    var placeLocation: String,
    var isFavorite: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)