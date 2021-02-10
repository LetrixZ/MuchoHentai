package com.letrix.muchohentai.app.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.letrix.muchohentai.app.network.ApiRepository
import com.letrix.muchohentai.app.network.JikanApiService
import com.letrix.muchohentai.app.room.cover.Cover
import com.letrix.muchohentai.app.room.cover.CoverRepository
import kotlinx.coroutines.launch

class MainViewModel
@ViewModelInject
constructor(
    private val repository: ApiRepository,
    private val jikanApiService: JikanApiService,
    private val coverRepository: CoverRepository
) :
    ViewModel() {

    fun post(postId: Int) = liveData { emit(repository.episode(postId)) }

    fun searchMal(query: String) = liveData { emit(jikanApiService.search(query)) }

    fun getCover(id: String) = liveData { emit(coverRepository.get(id)) }

    fun addCover(cover: Cover) =
        viewModelScope.launch {
            coverRepository.add(cover)
        }

}