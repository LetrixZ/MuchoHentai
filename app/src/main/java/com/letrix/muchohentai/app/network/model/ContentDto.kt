package com.letrix.muchohentai.app.network.model

import com.google.gson.annotations.SerializedName
import com.letrix.muchohentai.app.domain.Post

data class ContentDto(
    @SerializedName("newest") val recentReleases: List<PostDto>,
    @SerializedName("english") val recentRaws: List<PostDto>,
    @SerializedName("spanish") val recentEnglish: List<PostDto>,
    @SerializedName("raw") val recentSpanish: List<PostDto>,
    @SerializedName("preview") val recentPreviews: List<PostDto>,
)