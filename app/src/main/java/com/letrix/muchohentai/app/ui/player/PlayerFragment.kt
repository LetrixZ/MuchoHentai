package com.letrix.muchohentai.app.ui.player

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.ads.interactivemedia.v3.api.*
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.ima.ImaAdsLoader
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.text.CaptionStyleCompat
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.letrix.muchohentai.app.R
import com.letrix.muchohentai.app.databinding.FragmentPlayerBinding
import com.letrix.muchohentai.app.domain.Episode
import com.letrix.muchohentai.app.domain.Post
import com.letrix.muchohentai.app.room.cover.Cover
import com.letrix.muchohentai.app.util.ImageUtil
import timber.log.Timber


class PlayerFragment : Fragment(R.layout.fragment_player) {

    private val navArgs: PlayerFragmentArgs by navArgs()
    private val viewModel: PlayerViewModel by activityViewModels()
    lateinit var binding: FragmentPlayerBinding

    private lateinit var player: SimpleExoPlayer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayerBinding.bind(view)
        binding.aspectRatioLayout.setAspectRatio(16f / 9f)

        loadInfo()
    }

    private fun loadInfo() {
        binding.progressBar.visibility = View.VISIBLE
        val episode: Episode? = viewModel.get(navArgs.post.postId)
        if (episode != null)
            loadEpisodeInfo(episode)
        else
            viewModel.episode(navArgs.post.postId).observe(viewLifecycleOwner, {
                binding.progressBar.visibility = View.GONE
                if (it != null) {
                    loadEpisodeInfo(it)
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun loadEpisodeInfo(episode: Episode) {
        loadMalColver(navArgs.post)
        binding.episodeNumber.text =
            if (episode.type == "Episode") "Episode ${episode.episode.toString()}" else "Preview Episode ${episode.episode.toString()}"
        binding.episodeViews.text = "${episode.views} views"
        binding.seriesTitle.text = episode.series
        Timber.d(episode.streamUrl)
        initPlayer(episode)
    }

    private fun loadMalColver(post: Post) {
        viewModel.getCover(post.seriesId).observe(viewLifecycleOwner, { cover ->
            if (cover != null) {
                ImageUtil.loadImage(binding.cover, cover.cover)
            } else {
                viewModel.searchMal(post.series).observe(viewLifecycleOwner, {
                    viewModel.addCover(Cover(post.seriesId, post.series, it.results[0].image_url))
                    ImageUtil.loadImage(binding.cover, it.results[0].image_url)
                })
            }
        })
    }

    private fun initPlayer(episode: Episode) {
        val audioAttributes =
            AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MOVIE).setUsage(C.USAGE_MEDIA)
                .build()
        val trackSelector = DefaultTrackSelector(requireActivity())
        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
            Util.getUserAgent(
                requireActivity(), "Muchohentai App"
            ), null
        )
        httpDataSourceFactory.defaultRequestProperties["Referer"] = "https://muchohentai.com/"

        val imaAdsLoader = ImaAdsLoader.Builder(requireActivity()).setAdEventListener {
            AdEvent.AdEventListener { p0 -> Timber.d(p0.toString()) }
        }.setPlayAdBeforeStartPosition(true).build()
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(requireActivity(), null, httpDataSourceFactory)
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory)
            .setAdsLoaderProvider(object : AdsLoader, DefaultMediaSourceFactory.AdsLoaderProvider {
                override fun contentComplete() {

                }

                override fun getSettings(): ImaSdkSettings {
                    TODO("Not yet implemented")
                }

                override fun requestAds(p0: AdsRequest?) {
                    Timber.d(p0.toString())
                }

                override fun requestStream(p0: StreamRequest?): String {
                    return "https://a.adtng.com/get/10009120"
                }

                override fun addAdsLoadedListener(p0: AdsLoader.AdsLoadedListener?) {
                    Timber.d(p0.toString())
                }

                override fun removeAdsLoadedListener(p0: AdsLoader.AdsLoadedListener?) {
                    Timber.d(p0.toString())
                }

                override fun addAdErrorListener(p0: AdErrorEvent.AdErrorListener?) {
                    Timber.d(p0.toString())
                }

                override fun removeAdErrorListener(p0: AdErrorEvent.AdErrorListener?) {
                    Timber.d(p0.toString())
                }

                override fun getAdsLoader(adTagUri: Uri): com.google.android.exoplayer2.source.ads.AdsLoader {
                    return imaAdsLoader
                }
            })
            .setAdViewProvider(binding.playerView)
        player =
            SimpleExoPlayer.Builder(requireActivity()).setAudioAttributes(audioAttributes, true)
                .setTrackSelector(trackSelector)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()
        binding.playerView.player = player

        val defaultSubtitleColor = Color.argb(255, 218, 218, 218)
        val outlineColor = Color.argb(255, 43, 43, 43)
        val subtitleTypeface = Typeface.DEFAULT

        val style = CaptionStyleCompat(
            defaultSubtitleColor,
            Color.TRANSPARENT, Color.TRANSPARENT,
            CaptionStyleCompat.EDGE_TYPE_OUTLINE,
            outlineColor, subtitleTypeface
        )

        binding.playerView.subtitleView?.apply {
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
                MediaItem.Builder().setUri(Uri.parse(episode.streamUrl))
                    .setAdTagUri(Uri.parse("https://a.adtng.com/get/10009120"))
                    .setSubtitles(
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
            player.setMediaItem(
                MediaItem.Builder().setUri(Uri.parse(episode.streamUrl))
                    .setAdTagUri(Uri.parse("https://a.adtng.com/get/10009120")).build()
            )
        }

        imaAdsLoader.setPlayer(player)

        player.prepare()
        player.seekTo(0, C.TIME_UNSET)
        player.playWhenReady = false
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
                for (i in 0 until trackGroups.length) {
                    for (j in 0 until trackGroups[i].length) {
//                        Timber.d(trackGroups[i].getFormat(j).toString())
                    }
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        binding.playerProgressBar.visibility =
                            View.VISIBLE
                    }
                    Player.STATE_READY -> {
                        binding.playerProgressBar.visibility =
                            View.GONE
                    }
                    Player.STATE_ENDED, Player.STATE_IDLE -> {
                        binding.playerView.keepScreenOn = true
                    }
                }
            }
        })

        binding.miniControls.closePlayer.setOnClickListener {
        }
        binding.miniControls.exoNext.setOnClickListener { player.next() }
        binding.miniControls.exoPrev.setOnClickListener { player.previous() }
        binding.miniControls.exoPlayPause.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) player.pause()
            else player.play()
        }

//        initMediaSession(playlist)
//        initNotificationManager(playlist)

//        playerNotificationManager.setPlayer(player)

        binding.aspectRatioLayout.setAspectRatio(16f / 9f)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::player.isInitialized) {
            player.stop()
            player.clearMediaItems()
            player.release()
//            mediaSession.release()
        }
    }
}