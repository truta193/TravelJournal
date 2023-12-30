package com.truta.traveljournal.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.truta.traveljournal.database.IMemoryDao
import com.truta.traveljournal.model.Memory

class MemoryRepository(private val memoryDao: IMemoryDao) {
    val allMemories: LiveData<List<Memory>> = memoryDao.getAll()

    @WorkerThread
    suspend fun upsertMemory(memory: Memory) {
        memoryDao.upsert(memory)
    }

}