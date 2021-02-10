package com.letrix.muchohentai.app.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.letrix.muchohentai.app.domain.Content
import com.letrix.muchohentai.app.domain.Episode
import com.letrix.muchohentai.app.network.mapper.ContentMapper
import com.letrix.muchohentai.app.network.mapper.EpisodeMapper
import com.letrix.muchohentai.app.network.mapper.PostMapper
import com.letrix.muchohentai.app.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(
    private val apiService: ApiService,
    private val contentMapper: ContentMapper,
    private val episodeMapper: EpisodeMapper,
    private val postMapper: PostMapper
) {

    fun recent(): Flow<DataState<Content>> = flow {
        emit(DataState.Loading)
        try {
            val entity = apiService.latest()
            val domain = contentMapper.mapToDomain(entity)
            emit(DataState.Success(domain))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun episode(postId: Int): Episode {
        val entity = apiService.episode(postId)
        val domain = episodeMapper.mapToDomain(entity)
        return domain
    }

    /*suspend fun post(postId: Int): Flow<DataState<Post>> = flow {
        emit(DataState.Loading)
        try {
            val entity = apiService.post(postId)
            val domain = postMapper.mapToDomain(entity)
            emit(DataState.Success(domain))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }*/

    fun search(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostPaging(apiService, query, postMapper) }
        ).liveData

    /*suspend fun search(query: String): Flow<DataState<List<Post>>> = flow {
        emit(DataState.Loading)
        try {
            val entity = apiService.search(query)
            val domain = postMapper.mapToDomainList(entity)
            emit(DataState.Success(domain))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }*/

}