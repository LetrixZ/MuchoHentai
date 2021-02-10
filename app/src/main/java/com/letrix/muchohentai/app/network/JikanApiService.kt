package com.letrix.muchohentai.app.network

import com.letrix.muchohentai.app.network.model.JikanDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanApiService {

//    @GET("anime/{mal_id}/pictures")
//    suspend fun animeCover(@Path("mal_id") malId: Int): JikanDto
//
//    @GET("person/{mal_id}/pictures")
//    suspend fun artistCover(@Path("mal_id") malId: Int): JikanDto

    @GET("search/anime")
    suspend fun search(@Query("q") query: String) : JikanDto

}