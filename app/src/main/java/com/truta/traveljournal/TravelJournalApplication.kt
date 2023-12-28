package com.truta.traveljournal

import android.app.Application

class TravelJournalApplication: Application() {
    private val database by lazy {MemoryDatabase.getDatabase(this)}
    val repository by lazy {MemoryRepository(database.dao)}
}