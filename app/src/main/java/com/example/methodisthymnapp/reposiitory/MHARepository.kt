package com.example.methodisthymnapp.reposiitory

import com.example.methodisthymnapp.database.HymnDao
import javax.inject.Inject


class MHARepository @Inject constructor (private val hymnDao: HymnDao) {
    suspend fun allHymns() = hymnDao.getAllHymns()
    suspend fun getHymn(id: Int) = hymnDao.getHymn(id)
    suspend fun search(query: String) = hymnDao.search(query)
    suspend fun search(query: Int) = hymnDao.search(query)
}
