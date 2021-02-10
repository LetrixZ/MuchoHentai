package com.letrix.muchohentai.app.ui.player

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.letrix.muchohentai.app.domain.Episode
import com.letrix.muchohentai.app.network.ApiRepository
import com.letrix.muchohentai.app.network.JikanApiService
import com.letrix.muchohentai.app.room.cover.Cover
import com.letrix.muchohentai.app.room.cover.CoverRepository
import kotlinx.coroutines.launch

class PlayerViewModel
@ViewModelInject constructor(
    private val apiRepository: ApiRepository,
    private val coverRepository: CoverRepository,
    private val jikanApiService: JikanApiService,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun get(postId: Int) = savedStateHandle.get<Episode>(postId.toString())

    fun save(postId: Int, episode: Episode) = savedStateHandle.set(postId.toString(), episode)

    fun episode(postId: Int) = liveData { emit(apiRepository.episode(postId)) }

    fun searchMal(query: String) = liveData { emit(jikanApiService.search(query)) }

    fun getCover(id: String) = liveData { emit(coverRepository.get(id)) }

    fun addCover(cover: Cover) =
        viewModelScope.launch {
            coverRepository.add(cover)
        }

}