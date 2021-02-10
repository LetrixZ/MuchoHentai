package com.letrix.muchohentai.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.letrix.muchohentai.app.databinding.ItemPostBinding
import com.letrix.muchohentai.app.domain.Post
import com.letrix.muchohentai.app.ui.contentlist.ItemClickListener
import com.letrix.muchohentai.app.util.ImageUtil
import com.letrix.muchohentai.app.util.Util

class SearchPagingAdapter(
    private val itemClickListener: ItemClickListener,
) :
    PagingDataAdapter<Post, SearchPagingAdapter.ItemViewHolder>(POST_COMPARATOR) {

    companion object {
        private val POST_COMPARATOR = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.postUrl == newItem.postUrl
            }

        }
    }

    class ItemViewHolder(
        private val binding: ItemPostBinding,
        private val itemClickListener: ItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Post) {
            binding.apply {
                ImageUtil.loadImage(cover, item.thumbnail)
                name.text = item.series
                episode.text = Util.parseType(item.type, item.episode)
                language.text = Util.getLanguage(item.audioLanguage, item.subtitleLanguage, true)
                /*episode.text = Util.parseType(
                    item.type,
                    item.episode
                ) + " [${Util.getLanguage(item.audioLanguage, item.subtitleLanguage)}]"*/
                views.text = "${item.views} views"
                itemClick.setOnClickListener {
                    itemClickListener.onItemClick(item, cover)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        itemClickListener
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) holder.bind(item)
    }
}