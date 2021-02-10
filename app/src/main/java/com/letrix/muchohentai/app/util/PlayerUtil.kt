package com.letrix.muchohentai.app.util

import android.content.Context
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.letrix.muchohentai.app.R

object PlayerUtil {

    fun getMediaSession(context: Context): MediaSessionCompat {
        return MediaSessionCompat(context, "ExoPlayer").apply {
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
            isActive = true
        }
    }

    fun getMediaSessionConnector(
        playlist: String,
        mediaSession: MediaSessionCompat
    ): MediaSessionConnector {
        return MediaSessionConnector(mediaSession).apply {
            /*val queueNavigator = object : TimelineQueueNavigator(mediaSession) {
                override fun getMediaDescription(
                    player: Player,
                    windowIndex: Int,
                ): MediaDescriptionCompat {
                    val item = playlist[player.currentWindowIndex]
                    return MediaDescriptionCompat.Builder().apply {
                        setTitle("${item.theme.name} | ${item.anime.name}")
                        setDescription("${item.theme.name} (${item.theme.type})")
                    }.build()
                }
            }
            setQueueNavigator(queueNavigator)*/
        }
    }

    fun getPlayerNotificationManager(
        playlist: String,
        mediaSession: MediaSessionCompat,
        context: Context,
    ): PlayerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
        context,
        "Channel ID",
        R.string.app_name,
        R.string.app_name,
        R.string.app_name,
        DescriptionAdapter(playlist, context),
        object : PlayerNotificationManager.NotificationListener {
            override fun onNotificationCancelled(
                notificationId: Int,
                dismissedByUser: Boolean,
            ) {
            }
        }).apply {
        setUseNavigationActions(true)
        setUsePlayPauseActions(true)
        setUseStopAction(false)
        setUseChronometer(true)
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        setPriority(NotificationCompat.PRIORITY_LOW)
        setMediaSessionToken(mediaSession.sessionToken)
    }
}