package com.letrix.muchohentai.app.ui.contentlist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.letrix.muchohentai.app.domain.Content
import com.letrix.muchohentai.app.network.ApiRepository
import com.letrix.muchohentai.app.util.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ContentListViewModel
@ViewModelInject
constructor(
    private val repository: ApiRepository
) : ViewModel() {

    lateinit var contentList : Content

    private val _dataState: MutableLiveData<DataState<Any>> = MutableLiveData()

    val dataState: LiveData<DataState<Any>>
        get() = _dataState

    fun setStateEvent(event: ContentListEvent) {
        viewModelScope.launch {
            when (event) {
                is ContentListEvent.GetList -> {
                    val launchIn = repository.recent()
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

    sealed class ContentListEvent {
        object GetList : ContentListEvent()
    }

}