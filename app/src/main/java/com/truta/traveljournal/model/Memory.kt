package com.truta.traveljournal.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate
import java.util.Date

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
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)