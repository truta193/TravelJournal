package com.truta.traveljournal

import android.net.Uri
import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.truta.traveljournal.adapter.UriJsonAdapter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    private val moshi =
        Moshi.Builder()
            .add(Uri::class.java, UriJsonAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun fromUriList(uriList: List<Uri>?): String? {
        val type = Types.newParameterizedType(List::class.java, Uri::class.java)
        val adapter = moshi.adapter<List<Uri>>(type)
        return adapter.toJson(uriList)
    }

    @TypeConverter
    fun toUriList(uriString: String?): List<Uri>? {
        if (uriString.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, Uri::class.java)
        val adapter = moshi.adapter<List<Uri>>(type)
        return adapter.fromJson(uriString)
    }
}