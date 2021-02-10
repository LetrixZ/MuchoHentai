package com.letrix.muchohentai.app.ui

import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.text.CaptionStyleCompat
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.letrix.muchohentai.app.R
import com.letrix.muchohentai.app.databinding.ActivityMainBinding
import com.letrix.muchohentai.app.databinding.BottomPlayerSheetBinding
import com.letrix.muchohentai.app.domain.Episode
import com.letrix.muchohentai.app.util.PlayerUtil
import com.letrix.muchohentai.app.util.Util
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var playerBinding: BottomPlayerSheetBinding

    private lateinit var itemSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var playerSheet: BottomSheetBehavior<MotionLayout>

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        itemSheet = from(binding.bottomSheet.root)
        itemSheet.apply {
            skipCollapsed = true
            isHideable = true
            isFitToContents = false
            state = STATE_HIDDEN
        }

        playerSheet = from(binding.playerSheet.root)
        playerSheet.apply {
            isHideable = true
            skipCollapsed = false
            state = STATE_HIDDEN
        }

        playerBinding = binding.playerSheet
        playerBinding.root.setOnClickListener { playerSheet.state = STATE_EXPANDED }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, _, _ ->
            itemSheet.state = STATE_HIDDEN
        }
    }


//    fun playEpisode(post: Post) {
//        playerSheet.state = STATE_EXPANDED
//        playerBinding.progressBarRoot.visibility = View.VISIBLE
//        viewModel.post(post.postId).observe(this, {
//            itemSheet.state = STATE_HIDDEN
//            playerBinding.progressBarRoot.visibility = View.GONE
//            initPlayer(it)
//        })
//        viewModel.getCover(post.seriesId).observe(this, { cover ->
//            if (cover == null) {
//                Timber.d("Request not cached")
//                viewModel.searchMal(post.series).observe(this, {
//                    viewModel.addCover(
//                        Cover(
//                            id = post.seriesId,
//                            series = post.series,
//                            cover = it.results[0].image_url
//                        )
//                    )
//                    ImageUtil.loadImage(playerBinding.cover, it.results[0].image_url)
//                })
//            } else {
//                Timber.d("Request cached")
//                ImageUtil.loadImage(playerBinding.cover, cover.cover)
//            }
//        })
//    }


    fun hideKeyboard(v: View) {
        v.clearFocus()
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            v.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    override fun onBackPressed() {
        val fragmentManager =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        Timber.d(fragmentManager?.childFragmentManager?.backStackEntryCount.toString())
        if (playerSheet.state != STATE_HIDDEN) playerSheet()
        else super.onBackPressed()
    }

    private fun playerSheet() {
        val fragmentManager =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        Timber.d(fragmentManager?.childFragmentManager?.backStackEntryCount.toString())
        if (fragmentManager?.childFragmentManager?.backStackEntryCount != 0) {
            when (playerSheet.state) {
                STATE_COLLAPSED -> {
                    playerSheet.state = STATE_HIDDEN
                }
                STATE_EXPANDED -> {
                    playerSheet.state = STATE_COLLAPSED
                }
                STATE_HIDDEN -> {
                    super.onBackPressed()
                }
                STATE_SETTLING, STATE_HALF_EXPANDED, STATE_DRAGGING -> {
                }
            }
        } else {
            when (playerSheet.state) {
                STATE_COLLAPSED -> {
                    playerSheet.state = STATE_HIDDEN
                }
                STATE_EXPANDED -> {
                    playerSheet.state = STATE_COLLAPSED
                }
                STATE_HIDDEN -> {
                    super.onBackPressed()
                }
                STATE_SETTLING, STATE_HALF_EXPANDED, STATE_DRAGGING -> {
                }
            }
        }
    }

    private fun itemSheet() {
        when (itemSheet.state) {
            STATE_EXPANDED -> {
                itemSheet.state = STATE_HALF_EXPANDED
            }
            STATE_HALF_EXPANDED -> {
                itemSheet.state = STATE_HIDDEN
            }
            STATE_HIDDEN -> {
                super.onBackPressed()
            }
            STATE_SETTLING, STATE_COLLAPSED, STATE_DRAGGING -> {
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (playerSheet.state != STATE_HIDDEN)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                playerBinding.playerView.hideController()
                playerSheet.state = STATE_EXPANDED
                enterPictureInPictureMode(PictureInPictureParams.Builder().build())
            }
    }

    private lateinit var player: SimpleExoPlayer
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    private lateinit var playerNotificationManager: PlayerNotificationManager

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            playerBinding.aspectRatioLayout.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            Util.hideSystemUI(this)
        } else {
            playerBinding.aspectRatioLayout.resizeMode =
                AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
            Util.showSystemUI(this)
        }
    }

    private fun initPlayer(episode: Episode) {
        val audioAttributes =
            AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MOVIE).setUsage(C.USAGE_MEDIA)
                .build()
        val trackSelector = DefaultTrackSelector(this)
        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
            com.google.android.exoplayer2.util.Util.getUserAgent(
                applicationContext, "Muchohentai App"
            ), null
        )
        httpDataSourceFactory.defaultRequestProperties["Referer"] =
            "https://muchohentai.com/" // or maps
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(applicationContext, null, httpDataSourceFactory)
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
        player =
            SimpleExoPlayer.Builder(this).setAudioAttributes(audioAttributes, true)
                .setTrackSelector(trackSelector)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()
        playerBinding.playerView.player = player

        val defaultSubtitleColor = Color.argb(255, 218, 218, 218)
        val outlineColor = Color.argb(255, 43, 43, 43)
        val subtitleTypeface = Typeface.DEFAULT

        val style = CaptionStyleCompat(
            defaultSubtitleColor,
            Color.TRANSPARENT, Color.TRANSPARENT,
            CaptionStyleCompat.EDGE_TYPE_OUTLINE,
            outlineColor, subtitleTypeface
        )

        playerBinding.exoSubtitles.apply {
            setStyle(style)
            setFixedTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
            setPadding(0, 0, 0, 60)
        }

        trackSelector.setParameters(
            trackSelector
                .buildUponParameters()
                .setPreferredTextLanguage("und")
                .setSelectUndeterminedTextLanguage(true)
                .setDisabledTextTrackSelectionFlags(C.SELECTION_FLAG_FORCED)
                .setRendererDisabled(2, false)
        )

        Timber.d(episode.streamUrl)
        if (episode.subUrl != null) {
            Timber.d(episode.subUrl)
            player.setMediaItem(
                MediaItem.Builder().setUri(Uri.parse(episode.streamUrl)).setSubtitles(
                    listOf(
                        MediaItem.Subtitle(
                            Uri.parse(episode.subUrl),
                            MimeTypes.TEXT_VTT,
                            "und"
                        )
                    )
                ).build()
            )
        } else {
            player.setMediaItem(MediaItem.fromUri(Uri.parse(episode.streamUrl)))
        }

//        player.prepare()
        player.seekTo(0, C.TIME_UNSET)
        player.playWhenReady = false
        playerBinding.playerView.keepScreenOn = true

        player.addListener(object : Player.EventListener {
            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
                Timber.d(error)
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                playerBinding.miniControls.exoPlayPause.isChecked = !isPlaying
            }

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray,
            ) {
                super.onTracksChanged(trackGroups, trackSelections)
//                updateInfo(player.currentWindowIndex, playlist)
                for (i in 0 until trackGroups.length) {
                    for (j in 0 until trackGroups[i].length) {
                        Timber.d(trackGroups[i].getFormat(j).toString())
                    }
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        playerBinding.progressBar.visibility =
                            View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        playerBinding.progressBar.visibility =
                            View.GONE
                    }
                    Player.STATE_ENDED, Player.STATE_IDLE -> {
                        playerBinding.playerView.keepScreenOn = true
                    }
                }
            }
        })

        playerBinding.miniControls.closePlayer.setOnClickListener {
            playerSheet.state = STATE_HIDDEN
        }
        playerBinding.miniControls.exoNext.setOnClickListener { player.next() }
        playerBinding.miniControls.exoPrev.setOnClickListener { player.previous() }
        playerBinding.miniControls.exoPlayPause.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) player.pause()
            else player.play()
        }

//        initMediaSession(playlist)
//        initNotificationManager(playlist)

//        playerNotificationManager.setPlayer(player)

        playerBinding.aspectRatioLayout.setAspectRatio(16f / 9f)
        findViewById<ImageView>(R.id.fullscreen_button).setOnClickListener {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                this.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                this.requestedOrientation =
                    ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
            }
        }

        playerSheet.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_COLLAPSED -> {
                        playerBinding.playerView.useController = false
                        playerBinding.exoSubtitles.visibility = View.GONE
                    }
                    STATE_DRAGGING -> {
                        playerBinding.playerView.hideController()
                        playerBinding.exoSubtitles.visibility = View.GONE
                    }
                    STATE_EXPANDED -> {
                        playerBinding.playerView.useController = true
                        playerBinding.exoSubtitles.visibility = View.VISIBLE
                        if (!player.isPlaying) {
                            playerBinding.playerView.showController()
                        }
                    }
                    STATE_HALF_EXPANDED -> {
                    }
                    STATE_HIDDEN -> {
//                        releasePlayer()
                    }
                    STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= 0)
                    playerBinding.root.progress = 1 - slideOffset
            }
        })
    }

    /*private fun updateInfo(index: Int, playlist: Playlist) {
        val item = playlist[index]
        playerBinding.miniControls.apply {
            itemTitle.text = item.theme.name
            itemName.text = item.anime.name
        }
    }

    private fun initMediaSession(playlist: Playlist) {
        mediaSession = PlayerUtil.getMediaSession(this)
        mediaSessionConnector =
            PlayerUtil.getMediaSessionConnector(playlist, mediaSession)
    }

    private fun initNotificationManager(playlist: Playlist) {
        playerNotificationManager =
            PlayerUtil.getPlayerNotificationManager(playlist, mediaSession, this)
    }

    private fun releasePlayer() {
        player.stop()
        player.clearMediaItems()
//        playerNotificationManager.setPlayer(null)
//        mediaSessionConnector.setPlayer(null)
    }*/

    override fun onDestroy() {
        if (this::player.isInitialized) {
            player.release()
//            mediaSession.release()
        }
        super.onDestroy()
    }
}