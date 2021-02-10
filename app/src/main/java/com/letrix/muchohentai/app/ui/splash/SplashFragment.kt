package com.letrix.muchohentai.app.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.letrix.muchohentai.app.R
import com.letrix.muchohentai.app.domain.Post
import com.letrix.muchohentai.app.ui.contentlist.ContentListViewModel
import com.letrix.muchohentai.app.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val contentListViewModel: ContentListViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        contentListViewModel.setStateEvent(ContentListViewModel.ContentListEvent.GetList)
    }

    @Suppress("UNCHECKED_CAST")
    private fun subscribeObservers() {
        contentListViewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    contentListViewModel.contentList = dataState.data as List<Post.List>
                    findNavController().navigate(R.id.action_content_list)
                }
                is DataState.Error -> {
                    Timber.d(dataState.exception)
                }
                DataState.Loading -> {
                }
            }
        })
    }

}