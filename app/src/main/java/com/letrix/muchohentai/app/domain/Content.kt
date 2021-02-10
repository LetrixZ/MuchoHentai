package com.letrix.muchohentai.app.domain

import com.google.gson.annotations.SerializedName
import com.letrix.muchohentai.app.domain.Post

data class Content(
    val recentReleases: List<Post>,
    val recentRaws: List<Post>,
    val recentEnglish: List<Post>,
    val recentSpanish: List<Post>,
    val recentPreviews: List<Post>,
)