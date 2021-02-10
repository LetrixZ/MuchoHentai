package com.letrix.muchohentai.app.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.letrix.muchohentai.app.network.ApiRepository

class SearchViewModel
@ViewModelInject
constructor(
    private val repository: ApiRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    val currentQuery = state.getLiveData<String>("current_query")

    val results = currentQuery.switchMap { queryString ->
        repository.search(queryString).cachedIn(viewModelScope)
    }

    fun search(query: String) {
        currentQuery.value = query
    }

}