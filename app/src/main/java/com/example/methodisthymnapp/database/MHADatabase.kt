package com.example.methodisthymnapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [HymnEntity::class, HymnFts::class], version = 1, exportSchema = false)
abstract class MHADatabase : RoomDatabase() {
    abstract fun hymnDao(): HymnDao
}