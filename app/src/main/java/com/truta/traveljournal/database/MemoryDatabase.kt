package com.truta.traveljournal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.truta.traveljournal.model.Memory

@Database(
    entities = [Memory::class],
    version = 1
)
abstract class MemoryDatabase : RoomDatabase() {
    abstract val dao : IMemoryDao

    companion object {
        @Volatile
        private var INSTANCE : MemoryDatabase? = null

        fun getDatabase(context : Context) : MemoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MemoryDatabase::class.java,
                    "memory_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }

    }
}