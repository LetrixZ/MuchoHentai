package com.letrix.muchohentai.app.network.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @SerializedName("id") val post_id: Int,
    @SerializedName("series") val series: String,
    @SerializedName("series_id") val series_id: String,
    @SerializedName("uncensored") val uncensored: Boolean,
    @SerializedName("episode") val episode: Int,
    @SerializedName("audio") val audio_language: String,
    @SerializedName("subtitles") val subtitle_language: String,
    @SerializedName("type") val type: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("views") val views: Int,
    @SerializedName("cover") val cover: String,
    @SerializedName("url") val url: String,
    @SerializedName("tags") val tags: List<String>
)