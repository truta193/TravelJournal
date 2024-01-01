package com.truta.traveljournal.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.truta.traveljournal.model.Memory

@Dao
interface IMemoryDao {
    @Upsert
    suspend fun upsert(memory: Memory)

    @Delete
    suspend fun delete(memory: Memory)

    @Query("SELECT * FROM memories")
    fun getAll(): LiveData<List<Memory>>

//    @Query("SELECT * FROM memories WHERE id=:mId")
//    fun getById(mId: Int): Memory?

}