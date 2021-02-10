package com.letrix.muchohentai.app.network.model

import com.google.gson.annotations.SerializedName

data class EpisodeDto(
    @SerializedName("id") val post_id: Int,
    @SerializedName("series") val series: String,
    @SerializedName("series_id") val series_id: String,
    @SerializedName("episode") val episode: Int,
    @SerializedName("summary") val summary: String,
    @SerializedName("type") val type: String,
    @SerializedName("uncensored") val uncensored: Boolean,
    @SerializedName("views") val views: Int,
    @SerializedName("audio") val audio_language: String,
    @SerializedName("subtitles") val subtitle_language: String,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("stream_url") val stream_url: String,
    @SerializedName("sub_url") val sub_url: String
)
