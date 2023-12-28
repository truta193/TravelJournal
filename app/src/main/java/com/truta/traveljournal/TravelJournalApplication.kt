package com.truta.traveljournal

import android.app.Application
import com.truta.traveljournal.database.MemoryDatabase
import com.truta.traveljournal.repository.MemoryRepository

class TravelJournalApplication: Application() {
    private val database by lazy { MemoryDatabase.getDatabase(this)}
    val repository by lazy { MemoryRepository(database.dao) }
}