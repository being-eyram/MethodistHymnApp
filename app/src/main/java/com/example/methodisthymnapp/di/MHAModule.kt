package com.example.methodisthymnapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.methodisthymnapp.database.MHADatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MHAModule {

    @Provides
    @Singleton
    fun provideHymnDao(database: MHADatabase) = database.hymnDao()

    @Provides
    @Singleton
    fun provideHymnDatabase(@ApplicationContext context: Context): MHADatabase {
        return Room.databaseBuilder(
            context,
            MHADatabase::class.java,
            "hymn.db"
        ).createFromAsset("mhb.db")
            .addCallback(callback)
            .build()
    }

    private val callback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            db.execSQL(query)
        }
    }

    private const val query = """INSERT INTO hymns_fts(hymns_fts) 
                                 VALUES ('rebuild') """
}