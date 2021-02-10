package com.letrix.muchohentai.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.letrix.muchohentai.app.R
import com.letrix.muchohentai.app.databinding.ItemRowBinding
import com.letrix.muchohentai.app.databinding.ItemRowLinearBinding

class LinearConcatAdapter(
    private val context: Context,
    private val adapter: RecyclerView.Adapter<*>,
    private val header: String = ""
) :
    RecyclerView.Adapter<BaseConcatHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseConcatHolder<*> {
        val view = LayoutInflater.from(context).inflate(R.layout.item_row_linear, parent, false)
        val binding = ItemRowLinearBinding.bind(view)
        binding.recyclerView.setEmptyView(binding.noResult)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        return ConcatViewHolder(view)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: BaseConcatHolder<*>, position: Int) {
        when (holder) {
            is ConcatViewHolder -> holder.bind(adapter)
            else -> throw IllegalArgumentException("No viewholder to show this data, did you forgot to add it to the onBindViewHolder?")
        }
    }

    inner class ConcatViewHolder(itemView: View) :
        BaseConcatHolder<RecyclerView.Adapter<*>>(itemView) {
        private val binding = ItemRowLinearBinding.bind(itemView)
        override fun bind(adapter: RecyclerView.Adapter<*>) {
            binding.recyclerView.adapter = adapter
            binding.itemHeader.text = header
        }
    }
}