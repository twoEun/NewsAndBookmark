package com.kkc.news.local.roomData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookMark(
    @PrimaryKey(autoGenerate = true)
    val index: Int,

    @ColumnInfo(name = "author")
    val author: String?,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String?
)
