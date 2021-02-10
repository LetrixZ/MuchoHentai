package com.letrix.muchohentai.app.network.mapper

import com.letrix.muchohentai.app.domain.Post
import com.letrix.muchohentai.app.network.model.PostDto
import com.letrix.muchohentai.app.util.DomainMapper
import javax.inject.Inject

class PostMapper
@Inject
constructor() : DomainMapper<PostDto, Post> {

    override fun mapToDomain(model: PostDto): Post = Post(
        postId = model.post_id,
        series = model.series,
        seriesId = model.series_id,
        episode = model.episode,
        type = model.type,
        uncensored = model.uncensored,
        views = model.views,
        thumbnail = model.thumbnail,
        audioLanguage = model.audio_language,
        subtitleLanguage = model.subtitle_language,
        cover = model.cover,
        tags = model.tags,
        postUrl = model.url
    )

    override fun mapFromDomain(domainModel: Post): PostDto {
        TODO("Not yet implemented")
    }

    fun mapToDomainList(list: List<PostDto>): List<Post> =
        list.map { mapToDomain(it) }


}