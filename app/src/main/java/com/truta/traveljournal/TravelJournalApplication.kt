package com.truta.traveljournal

import android.app.Application
import com.truta.traveljournal.database.MemoryDatabase
import com.truta.traveljournal.repository.MemoryRepository
import com.truta.traveljournal.service.ThemeService

class TravelJournalApplication : Application() {
    private val database by lazy { MemoryDatabase.getDatabase(this) }
    val repository by lazy { MemoryRepository(database.dao) }

    override fun onCreate() {
        super.onCreate()
        ThemeService.applyTheme(this)
    }

}