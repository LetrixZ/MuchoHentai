package com.letrix.muchohentai.app.util

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.letrix.muchohentai.app.ui.MainActivity
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class DescriptionAdapter(var playlist: String, var context: Context) :
    MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): String {
//        return "${playlist.last().theme.name} (${playlist.last().theme.type})"
        return ""
    }

    override fun getCurrentContentText(player: Player): String {
//        return playlist.last().anime.name
        return ""
    }

    override fun getCurrentLargeIcon(player: Player, callback: BitmapCallback): Bitmap? {
        val bitmap = arrayOfNulls<Bitmap>(1)

        /*Picasso.get().load(playlist.last().anime.cover).into(object : Target {
            override fun onBitmapLoaded(image: Bitmap?, from: Picasso.LoadedFrom?) {
                image?.let { callback.onBitmap(it) }
                bitmap[0] = image
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

        })*/

        return bitmap[0]
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.putExtra("player", true)
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

}