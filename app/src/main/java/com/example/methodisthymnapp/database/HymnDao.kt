package com.example.methodisthymnapp.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface HymnDao {
    @Query("SELECT * FROM hymns_table")
    suspend fun getAllHymns(): List<HymnEntity>

    @Query("SELECT * FROM hymns_table WHERE :id = _id ")
    suspend fun getHymn(id: Int): HymnEntity

    @Query(
        """SELECT *
                 FROM hymns_table
                 JOIN hymns_fts ON hymns_fts.rowid = hymns_table._id  
                 WHERE hymns_fts MATCH :query"""
    )
    suspend fun search(query: String): List<HymnEntity>

    /**
     * Search Hymns by their numbers
     */

    @Query(
        """SELECT * 
                 FROM hymns_table
                 WHERE _id LIKE '%' || :query || '%' """
    )
    suspend fun search(query: Int): List<HymnEntity>
}