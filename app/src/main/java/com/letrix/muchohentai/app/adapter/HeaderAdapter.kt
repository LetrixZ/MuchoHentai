package com.letrix.muchohentai.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.letrix.muchohentai.app.R
import com.letrix.muchohentai.app.databinding.ItemHeaderBinding

class HeaderAdapter(private val title: String?) :
    RecyclerView.Adapter<HeaderAdapter.HeaderHolder>() {
    inner class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemHeaderBinding.bind(itemView)
        fun bind(title: String) {
            binding.itemHeader.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderHolder =
        HeaderHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
        )

    override fun onBindViewHolder(holder: HeaderHolder, position: Int) = holder.bind(title ?: "")

    override fun getItemCount() = 1
}