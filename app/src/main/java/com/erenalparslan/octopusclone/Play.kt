package com.erenalparslan.octopusclone

import android.app.Activity
import android.media.MediaCodec
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.erenalparslan.octopusclone.databinding.FragmentPlayBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer


class Play : Fragment() {

    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var fragmentPlayBinding :FragmentPlayBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding=FragmentPlayBinding.bind(view)
        fragmentPlayBinding=binding
            preparePlayer()
    }


    /*private fun preparePlayer() {
        val bandwidthMeter = DefaultBandwidthMeter.Builder(requireContext()).build()
        val trackSelector = DefaultTrackSelector(requireContext())
        trackSelector.parameters = trackSelector.buildUponParameters()
            .setMaxVideoSizeSd() // maksimum 480p kaliteye kadar destekleyen cihazlar için
            //.setMaxVideoSize(1080, 1920) // Full HD (1080p) kaliteyi destekleyen cihazlar için
            .build()
        exoPlayer = SimpleExoPlayer.Builder(requireContext())
            .setTrackSelector(trackSelector)
            .build()
        exoPlayer?.playWhenReady = true
        fragmentPlayBinding?.playerView?.player = exoPlayer
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val mediaItem = MediaItem.fromUri(URL)
        val mediaSource =
            DashMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
        exoPlayer?.setMediaSource(mediaSource)
        exoPlayer?.seekTo(playbackPosition)
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.prepare()
    }*/

        private fun preparePlayer() {




          val bandwidthMeter = DefaultBandwidthMeter.Builder(requireContext()).build()
            val trackSelectionFactory = AdaptiveTrackSelection.Factory()
            val trackSelector = DefaultTrackSelector(requireContext(),trackSelectionFactory)
            exoPlayer =ExoPlayer.Builder(requireContext())
                .setTrackSelector(trackSelector)
                .build()
            exoPlayer?.playWhenReady = true
            fragmentPlayBinding?.playerView?.player = exoPlayer
            val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
            val mediaItem = MediaItem.fromUri(URL)
            val mediaSource =
                DashMediaSource.Factory(defaultHttpDataSourceFactory).createMediaSource(mediaItem)
            exoPlayer?.setMediaSource(mediaSource)
            exoPlayer?.seekTo(playbackPosition)
            exoPlayer?.playWhenReady = playWhenReady
            exoPlayer?.prepare()
        }

    private fun relasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }

    override fun onStop() {
        super.onStop()
        relasePlayer()
    }

    override fun onPause() {
        super.onPause()
        relasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        relasePlayer()
    }

    companion object {
        const val URL =
            "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd"
    }


}