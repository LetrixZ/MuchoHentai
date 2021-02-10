package com.letrix.muchohentai.app.ui.contentlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.letrix.muchohentai.app.R
import com.letrix.muchohentai.app.databinding.ItemAnimeBinding
import com.letrix.muchohentai.app.domain.Post
import com.letrix.muchohentai.app.ui.search.SearchFragment
import com.letrix.muchohentai.app.util.ImageUtil
import com.letrix.muchohentai.app.util.Util

class ContentListAdapter(
    private val itemClickListener: ItemClickListener,
    private val layout: Int = R.layout.item_anime
) :
    ListAdapter<Post, ContentListAdapter.ContentHolder>(object :
        DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.postId == newItem.postId

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContentHolder(
        LayoutInflater.from(parent.context).inflate(layout, parent, false),
        itemClickListener
    )

    override fun onBindViewHolder(holder: ContentHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ContentHolder(itemView: View, private val itemClickListener: ItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val binding = ItemAnimeBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(item: Post) {
            binding.apply {
                ImageUtil.loadImage(cover, item.thumbnail)
                name.text = item.series
                episode.text =
                    if (item.type == "Episode") "Episode ${item.episode}" else "PV Episode ${item.episode}"
                episode.append(" [${Util.getLanguage(item.audioLanguage, item.subtitleLanguage)}]")
                itemClick.setOnClickListener {
                    itemClickListener.onItemClick(item, cover)
                }
            }
        }
    }

}