package com.letrix.muchohentai.app.network

import com.letrix.muchohentai.app.domain.Post
import com.letrix.muchohentai.app.network.model.EpisodeDto
import com.letrix.muchohentai.app.network.model.PostDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(API + "post/latest")
    suspend fun latest(): List<Post.List>

    @GET(API + "post/{id}")
    suspend fun episode(@Path("id") postId: Int): EpisodeDto

    @GET(API + "post/search")
    suspend fun search(@Query("q") query: String, @Query("page") page: Int = 1): List<PostDto>

    companion object {
        const val API = "api/v1/"
    }

}