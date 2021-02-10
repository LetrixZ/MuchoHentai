package com.letrix.muchohentai.app.room.cover

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoverRepository @Inject constructor(
    private val dao: CoverDao
) {

    val all: Flow<List<Cover>> = dao.all()

    suspend fun add(cover: Cover) {
        dao.add(cover)
    }

    suspend fun removeAll() = dao.deleteAll()

    suspend fun remove(cover: Cover) {
        dao.remove(cover)
    }

    suspend fun get(id: String) =
        dao.get(id)
}
