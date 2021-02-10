package com.letrix.muchohentai.app.network.mapper

import com.letrix.muchohentai.app.domain.Episode
import com.letrix.muchohentai.app.network.model.EpisodeDto
import com.letrix.muchohentai.app.util.DomainMapper
import javax.inject.Inject

class EpisodeMapper
@Inject
constructor() : DomainMapper<EpisodeDto, Episode> {

    override fun mapToDomain(model: EpisodeDto): Episode = Episode(
        postId = model.post_id,
        series = model.series,
        seriesId = model.series_id,
        episode = model.episode,
        summary = model.summary,
        type = model.type,
        views = model.views,
        audioLanguage = model.audio_language,
        subtitleLanguage = model.subtitle_language,
        tags = model.tags,
        streamUrl = model.stream_url,
        subUrl = model.sub_url,
        cover = null
    )

    override fun mapFromDomain(domainModel: Episode): EpisodeDto {
        TODO("Not yet implemented")
    }

    fun mapToDomainList(list: List<EpisodeDto>): List<Episode> =
        list.map { mapToDomain(it) }


}