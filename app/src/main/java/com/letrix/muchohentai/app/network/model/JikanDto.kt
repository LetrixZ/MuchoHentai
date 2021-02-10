package com.letrix.muchohentai.app.network.model

import com.google.gson.annotations.SerializedName

data class JikanDto(
    @SerializedName("request_hash") val request_hash: String,
    @SerializedName("request_cached") val request_cached: Boolean,
    @SerializedName("request_cache_expiry") val request_cache_expiry: Int,
    @SerializedName("results") val results: List<Results>,
    @SerializedName("last_page") val last_page: Int
) {
    data class Results(
        @SerializedName("mal_id") val mal_id: Int,
        @SerializedName("url") val url: String,
        @SerializedName("image_url") val image_url: String,
        @SerializedName("title") val title: String,
        @SerializedName("airing") val airing: Boolean,
        @SerializedName("synopsis") val synopsis: String,
        @SerializedName("type") val type: String,
        @SerializedName("episodes") val episodes: Int,
        @SerializedName("score") val score: Double,
        @SerializedName("start_date") val start_date: String,
        @SerializedName("end_date") val end_date: String,
        @SerializedName("members") val members: Int,
        @SerializedName("rated") val rated: String
    )
}
