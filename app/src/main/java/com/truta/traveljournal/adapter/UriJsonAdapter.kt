package com.truta.traveljournal.adapter

import android.net.Uri
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import okio.IOException

class UriJsonAdapter : JsonAdapter<Uri>() {
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Uri? {
        return if (reader.peek() == JsonReader.Token.STRING) {
            Uri.parse(reader.nextString())
        } else {
            null
        }
    }

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: Uri?) {
        writer.value(value?.toString())
    }
}