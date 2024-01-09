package com.truta.traveljournal.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
@Entity(tableName = "memories")
data class Memory(
    var title: String,
    var placeName: String?,
    var placeLatitude: Double?,
    var placeLongitude: Double?,
    var isFavorite: Boolean,
    var date: LocalDate,
    var type: String,
    var mood: Double,
    var notes: String,
    var pictures: List<String>?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)