package com.letrix.muchohentai.app.ui.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.letrix.muchohentai.app.R
import com.letrix.muchohentai.app.adapter.SearchLoadStateAdapter
import com.letrix.muchohentai.app.adapter.SearchPagingAdapter
import com.letrix.muchohentai.app.databinding.FragmentSearchBinding
import com.letrix.muchohentai.app.domain.Post
import com.letrix.muchohentai.app.ui.contentlist.ItemClickListener
import com.letrix.muchohentai.app.util.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search), ItemClickListener {

    private lateinit var inputMethodManager: InputMethodManager

    private val searchViewModel: SearchViewModel by activityViewModels()

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSearchBinding.bind(view)

        binding.header.backButton.setOnClickListener { findNavController().popBackStack() }
        binding.header.editBox.visibility = View.VISIBLE

        searchViewModel.currentQuery.observe(viewLifecycleOwner, {
            binding.header.editBox.setText(it)
        })

        val adapter = SearchPagingAdapter(this)
        (binding.recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.itemCount == position) 2 else 1
                }
            }

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter =
                adapter.withLoadStateHeaderAndFooter(
                    header = SearchLoadStateAdapter { adapter.retry() },
                    footer = SearchLoadStateAdapter { adapter.retry() })
            retryButton.setOnClickListener { adapter.retry() }
            recyclerView.addItemDecoration(GridSpacingItemDecoration(2, 0, false))
        }

        searchViewModel.results.observe(viewLifecycleOwner, {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                retryButton.isVisible = loadState.source.refresh is LoadState.Error
                errorMessage.isVisible = loadState.source.refresh is LoadState.Error
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount < 1) {
                    recyclerView.isVisible = false
                    noResults.isVisible = true
                } else noResults.isVisible = false
            }
        }

        inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.header.editBox.setOnEditorActionListener { _, i, _ ->
            binding.header.editBox.clearFocus()
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.header.editBox.text.toString()
                inputMethodManager.hideSoftInputFromWindow(
                    binding.header.editBox.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
//                searchViewModel.setStateEvent(SearchEvent.SearchContent(query))
                binding.recyclerView.scrollToPosition(0)
                searchViewModel.search(query)
                binding.header.editBox.clearFocus()
                true
            } else false
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onItemClick(item: Post, image: ImageView) {
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            bundleOf("post" to item)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}