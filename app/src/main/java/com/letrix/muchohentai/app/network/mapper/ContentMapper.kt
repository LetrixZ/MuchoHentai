package com.letrix.muchohentai.app.network.mapper

import com.letrix.muchohentai.app.domain.Content
import com.letrix.muchohentai.app.domain.Post
import com.letrix.muchohentai.app.network.model.ContentDto
import com.letrix.muchohentai.app.network.model.PostDto
import com.letrix.muchohentai.app.util.DomainMapper
import javax.inject.Inject

class ContentMapper
@Inject
constructor(private val postMapper: PostMapper) : DomainMapper<ContentDto, Content> {

    override fun mapToDomain(model: ContentDto): Content = Content(
        recentPreviews = postMapper.mapToDomainList(model.recentPreviews),
        recentEnglish = postMapper.mapToDomainList(model.recentEnglish),
        recentRaws = postMapper.mapToDomainList(model.recentRaws),
        recentReleases = postMapper.mapToDomainList(model.recentReleases),
        recentSpanish = postMapper.mapToDomainList(model.recentSpanish)
    )

    override fun mapFromDomain(domainModel: Content): ContentDto {
        TODO("Not yet implemented")
    }

    fun mapToDomainList(list: List<ContentDto>): List<Content> =
        list.map { mapToDomain(it) }


}