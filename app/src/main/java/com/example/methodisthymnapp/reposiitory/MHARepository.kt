package com.example.methodisthymnapp.reposiitory

import com.example.methodisthymnapp.database.HymnDao
import javax.inject.Inject


class MHARepository @Inject constructor(private val hymnDao: HymnDao) {
    fun allHymns() = hymnDao.getAllHymns()
    fun getFavorites() = hymnDao.getFavorites()
    fun search(query: String) = hymnDao.search(query)
    fun search(query: Int) = hymnDao.search(query)
    suspend fun getHymn(id: Int) = hymnDao.getHymn(id)
    suspend fun updateFavoriteState(id: Int, isFavorite: Int) =
        hymnDao.updateFavoriteState(id, isFavorite)
}
