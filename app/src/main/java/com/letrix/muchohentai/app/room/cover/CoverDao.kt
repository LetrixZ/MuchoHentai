package com.letrix.muchohentai.app.room.cover

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CoverDao {

    @Query("DELETE from cover_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(cover: Cover)

    @Delete
    suspend fun remove(bookmark: Cover)

    @Query("SELECT * FROM cover_table where id == :id")
    suspend fun get(id: String): Cover

    @Query("SELECT * FROM cover_table ORDER BY id ASC")
    fun all(): Flow<List<Cover>>

}