package com.example.methodisthymnapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey


@Entity(tableName = "hymns_table")
data class HymnEntity(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "lyrics")
    val lyrics: String,
)

@Entity(tableName = "hymns_fts")
@Fts4(contentEntity = HymnEntity::class)
data class HymnFts(

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "lyrics")
    val lyrics: String
)