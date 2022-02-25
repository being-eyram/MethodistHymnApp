package com.example.methodisthymnapp.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HymnDao {
    /** Fetch all Hymns from Hymn Database */
    @Query("SELECT * FROM hymns_table")
    fun getAllHymns(): Flow<List<Hymn>>

    /** Fetch Hymn associated with [id] provided*/
    @Query("SELECT * FROM hymns_table WHERE :id = _id ")
    suspend fun getHymn(id: Int): Hymn

    /** Fetch all Favorite Hymns*/
    @Query("SELECT * FROM hymns_table WHERE is_favorite = 1")
    fun getFavorites(): Flow<List<Hymn>>

    /** Search Hymns by author, lyrics or title */
    @Query(
        """SELECT *
                 FROM hymns_table
                 JOIN hymns_fts ON hymns_fts.rowid = hymns_table._id  
                 WHERE hymns_fts MATCH :query"""
    )
    fun search(query: String): Flow<List<Hymn>>

   /** Toggle favorite for hymn [id] provided*/
    @Query(
        """UPDATE hymns_table 
                 SET is_favorite = :isFavorite 
                 WHERE _id = :id"""
    )
    suspend fun updateFavoriteState(id: Int, isFavorite: Int)

    /** Search Hymns by their numbers*/
    @Query(
        """SELECT * 
                 FROM hymns_table
                 WHERE _id LIKE '%' || :query || '%' """
    )
    fun search(query: Int): Flow<List<Hymn>>
}