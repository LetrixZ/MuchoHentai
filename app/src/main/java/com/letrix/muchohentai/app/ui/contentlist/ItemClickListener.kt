package com.letrix.muchohentai.app.ui.contentlist

import android.widget.ImageView
import com.letrix.muchohentai.app.domain.Post

interface ItemClickListener {

    fun onItemClick(item: Post, image: ImageView)
}