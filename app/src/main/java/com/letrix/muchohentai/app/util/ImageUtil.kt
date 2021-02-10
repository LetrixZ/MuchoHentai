package com.letrix.muchohentai.app.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

object ImageUtil {

    fun loadImage(view: ImageView, data: String?) {
        Picasso.get().setIndicatorsEnabled(true)
        Picasso.get().load(data).into(view)
    }

    fun asyncLoad(data: String?, context: Context) : Drawable {
        val bitmap = arrayOfNulls<Bitmap>(1)
        Picasso.get().load(data).into(object : Target {
            override fun onBitmapLoaded(image: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap[0] = image
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

        })
        return BitmapDrawable(context.resources, bitmap[0])
    }

}