package com.truta.traveljournal.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.Observable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.util.Date

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