package com.letrix.muchohentai.app.ui.contentlist

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.letrix.muchohentai.app.R
import com.letrix.muchohentai.app.adapter.LinearConcatAdapter
import com.letrix.muchohentai.app.databinding.FragmentContentlistBinding
import com.letrix.muchohentai.app.domain.Post
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentListFragment : Fragment(R.layout.fragment_contentlist), ItemClickListener {

    private val viewModel: ContentListViewModel by activityViewModels()

    private var _binding: FragmentContentlistBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentContentlistBinding.bind(view)
        displayData(viewModel.contentList)

        binding.apply {
            search.setOnClickListener {
                findNavController().navigate(R.id.to_search)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayData(list: List<Post.List>) {
        val concatAdapter = ConcatAdapter()

        val recentRelease = ContentListAdapter(this)
        recentRelease.submitList(list[0].list)
        val recentReleaseConcat = LinearConcatAdapter(
            requireActivity(), recentRelease, "Recent Uploads"
        )
        concatAdapter.addAdapter(recentReleaseConcat)

        val recentEnglish = ContentListAdapter(this)
        recentEnglish.submitList(list[1].list)
        val recentEnglishConcat = LinearConcatAdapter(
            requireActivity(), recentEnglish, "Recent English sub"
        )
        concatAdapter.addAdapter(recentEnglishConcat)

        val recentSpanish = ContentListAdapter(this)
        recentSpanish.submitList(list[2].list)
        val recentSpanishConcat = LinearConcatAdapter(
            requireActivity(), recentSpanish, "Recent Spanish sub"
        )
        concatAdapter.addAdapter(recentSpanishConcat)

        val recentRaws = ContentListAdapter(this)
        recentRaws.submitList(list[3].list)
        val recentRawsConcat = LinearConcatAdapter(
            requireActivity(), recentRaws, "Recent Raws"
        )
        concatAdapter.addAdapter(recentRawsConcat)

        val recentPreviews = ContentListAdapter(this)
        recentPreviews.submitList(list[4].list)
        val recentPreviewsConcat = LinearConcatAdapter(
            requireActivity(), recentPreviews, "Recent Previews"
        )
        concatAdapter.addAdapter(recentPreviewsConcat)

        binding.recyclerView.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onItemClick(item: Post, image: ImageView) {
        findNavController().navigate(
            R.id.action_contentListFragment_to_playerFragment,
            bundleOf("post" to item)
        )
    }

}