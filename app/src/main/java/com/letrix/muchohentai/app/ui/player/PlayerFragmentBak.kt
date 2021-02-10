package com.letrix.muchohentai.app.ui.player

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.letrix.muchohentai.app.R
import com.letrix.muchohentai.app.databinding.FragmentPlayerBinding
import com.letrix.muchohentai.app.ui.MainActivity
import com.letrix.muchohentai.app.util.PlayerUtil
import com.letrix.muchohentai.app.util.Util
import timber.log.Timber
import java.nio.channels.NonReadableChannelException

class PlayerFragmentBak : Fragment(R.layout.fragment_player) {

    private lateinit var binding: FragmentPlayerBinding

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var playerNotificationManager: PlayerNotificationManager

    private lateinit var mainActivity: MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerBinding.bind(view)

        mainActivity = (activity as MainActivity)

        initPlayer()

        binding.aspectRatioLayout.setAspectRatio(16f / 9f)
        view.findViewById<ImageView>(R.id.fullscreen_button).setOnClickListener {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity?.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.aspectRatioLayout.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            Util.hideSystemUI(requireActivity())
        } else {
            binding.aspectRatioLayout.resizeMode =
                AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
            Util.showSystemUI(requireActivity())
        }
    }

    private fun initPlayer() {
        binding.root.progress = 1f
        val audioAttributes =
            AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MOVIE).setUsage(C.USAGE_MEDIA)
                .build()
        player =
            SimpleExoPlayer.Builder(requireActivity()).setAudioAttributes(audioAttributes, true)
                .build()
        binding.playerView.player = player

        val mediaItems: ArrayList<MediaItem> = ArrayList()
        player.setMediaItems(mediaItems)
        player.prepare()
        player.seekTo(0, C.TIME_UNSET)
        player.playWhenReady = true
        binding.playerView.keepScreenOn = true

        player.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                Timber.d(error)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                binding.miniControls.exoPlayPause.isChecked = !isPlaying
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray,
            ) {
                super.onTracksChanged(trackGroups, trackSelections)
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        binding.progressBar.visibility =
                            View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        binding.progressBar.visibility =
                            View.GONE
                    }
                    Player.STATE_ENDED, Player.STATE_IDLE -> {
                        binding.playerView.keepScreenOn = true
                    }
                }
            }
        })

        binding.miniControls.closePlayer.setOnClickListener {
//            mainActivity.sheet().state = STATE_HIDDEN
        }
        binding.miniControls.exoNext.setOnClickListener { player.next() }
        binding.miniControls.exoPrev.setOnClickListener { player.previous() }
        binding.miniControls.exoPlayPause.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) player.pause()
            else player.play()
        }

        initMediaSession()
        initNotificationManager()

        playerNotificationManager.setPlayer(player)
    }


    private fun initMediaSession() {
        mediaSession = PlayerUtil.getMediaSession(requireActivity())
//        mediaSessionConnector = PlayerUtil.getMediaSessionConnector(playlist, mediaSession)
    }

    private fun initNotificationManager() {
//        playerNotificationManager = PlayerUtil.getPlayerNotificationManager(playlist, mediaSession, requireActivity())
    }

    private fun releasePlayer() {
        player.stop()
        player.clearMediaItems()
        player.release()
        playerNotificationManager.setPlayer(null)
        mediaSession.release()
        mediaSessionConnector.setPlayer(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::player.isInitialized) {
            releasePlayer()
        }
    }
}