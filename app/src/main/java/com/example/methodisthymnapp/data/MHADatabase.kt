package com.example.methodisthymnapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Hymn::class, HymnFts::class], version = 1, exportSchema = false)
abstract class MHADatabase : RoomDatabase() {
    abstract fun hymnDao(): HymnDao
}