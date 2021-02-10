package com.letrix.muchohentai.app.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.letrix.muchohentai.app.domain.Post
import com.letrix.muchohentai.app.network.mapper.PostMapper
import retrofit2.HttpException
import java.io.IOException

class PostPaging(
    private val apiService: ApiService,
    private val query: String,
    private val postMapper: PostMapper
) : PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val position = params.key ?: 1
        return try {
            val response = apiService.search(query, position)
            val domain = postMapper.mapToDomainList(response)
            LoadResult.Page(
                data = domain,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (domain.isEmpty()) null else position + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

}