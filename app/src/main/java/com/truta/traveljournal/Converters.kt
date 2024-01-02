package com.truta.traveljournal

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    private val moshi = Moshi.Builder().build()
    private val listType: Type = Types.newParameterizedType(List::class.java, String::class.java)
    private val adapter: JsonAdapter<List<String>> = moshi.adapter(listType)


    @TypeConverter
    fun fromTimestamp(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return if (value == null) {
            null
        } else {
            adapter.fromJson(value)
        }
    }

    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.let { adapter.toJson(it) }
    }
}