package com.letrix.muchohentai.app.room.cover

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cover_table")
data class Cover(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val series: String,
    val cover: String,
)