package com.erenalparslan.octopusclone


import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

import com.erenalparslan.octopusclone.databinding.FragmentPlayBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.EventLogger


class Play : Fragment() {
    private var currentView: View? = null
    private val KEY_TRACK_SELECTION_PARAMETERS = "track_selection_parameters"
    private val KEY_ITEM_INDEX = "item_index"
    private val KEY_POSITION = "position"
    private val KEY_AUTO_PLAY = "auto_play"

    private var fragmentPlayBinding: FragmentPlayBinding? = null
    private var exoPlayer: ExoPlayer? = null
    private var playbackPosition = 0L
    private var playWhenReady = true
    private var player: ExoPlayer? = null
    private var playerView: StyledPlayerView? = null
    private var fullscreenButton: ImageView? = null

    private var exoPlay: ImageView? = null
    private var exoPause: ImageView? = null
    private var exoRestart: ImageView? = null
    private var volumeBtn: ImageView? = null
    private var ivVideoQuality: ImageView? = null

    private var exoPosition: TextView? = null
    private var exoDuration: TextView? = null

    private var lastSeenTracks: Tracks? = null
    var isShowingTrackSelectionDialog: Boolean = false
    private var trackSelectionParameters: TrackSelectionParameters? = null

    private var startAutoPlay = false
    private var startItemIndex = 0
    private var startPosition: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_play, container, false)
        return currentView

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
   /*     println(currentView)
        val inflater = LayoutInflater.from(requireContext())
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            currentView = inflater.inflate(R.layout.fragment_play, null)
            //  activity?.setContentView(currentView)
        } else {

        }*/
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPlayBinding.bind(view)
        fragmentPlayBinding = binding


        if (savedInstanceState != null) {
            trackSelectionParameters = TrackSelectionParameters.fromBundle(
                savedInstanceState.getBundle(KEY_TRACK_SELECTION_PARAMETERS)!!
            )
            startAutoPlay = savedInstanceState.getBoolean(KEY_AUTO_PLAY)
            startItemIndex = savedInstanceState.getInt(KEY_ITEM_INDEX)

        } else {
            trackSelectionParameters =
                TrackSelectionParameters.Builder( /* context= */requireContext()).build()

        }
        preparePlayer()
    }


    private fun preparePlayer() {
        //  exoPlayer = ExoPlayer.Builder(this).build()

        playerView = requireActivity().findViewById(R.id.playerView)


        fullscreenButton = playerView?.findViewById(R.id.bt_full)
        exoPlay = playerView?.findViewById(R.id.exo_play)
        exoPause = playerView?.findViewById(R.id.exo_pause)
        exoPosition = playerView?.findViewById(R.id.exo_position)
        exoDuration = playerView?.findViewById(R.id.exo_duration)
        exoRestart = playerView?.findViewById(R.id.exo_restart)
        volumeBtn = playerView?.findViewById(R.id.volumebtn)
        ivVideoQuality = playerView?.findViewById(R.id.ivVideoQuality)


        lastSeenTracks = Tracks.EMPTY
        exoPlayer = ExoPlayer.Builder(requireContext())
            //   .setTrackSelector(trackSelector)
            .build()
        exoPlayer!!.trackSelectionParameters = trackSelectionParameters!!
        exoPlayer!!.addListener(PlayerEventListener())
        exoPlayer!!.addAnalyticsListener(EventLogger())
        exoPlayer?.playWhenReady = true
        fragmentPlayBinding?.playerView?.player = exoPlayer


        val mediaItem = MediaItem.fromUri(URL)

        exoPlayer?.addMediaItem(mediaItem)
        exoPlayer?.seekTo(playbackPosition)
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.prepare()
        //  playerView?.player = player


/*        fun showQualitySelector() {
            val trackSelector = exoPlayer?.trackSelector
            if (trackSelector != null) {
                val trackSelectionView = TrackSelectionView(requireContext())
                trackSelectionView.setShowDisableOption(false)
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Select Quality")
                    .setView(trackSelectionView)
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        // OK button clicked, do something
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                builder.show()
            }
        }*/

        ivVideoQuality?.setOnClickListener {

            isShowingTrackSelectionDialog = true
            val trackSelectionDialog = TrackSelectionDialog.createForPlayer(
                exoPlayer
            )  /* onDismissListener= */
            { dismissedDialog -> isShowingTrackSelectionDialog = false }
            trackSelectionDialog.show(requireActivity().supportFragmentManager,  /* tag= */null)
            /* }*/
        }
    }


    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            exoPlayer = null
        }
    }

    private fun updateTrackSelectorParameters() {
        if (player != null) {
            trackSelectionParameters = player!!.trackSelectionParameters
        }
    }

    private class PlayerEventListener : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: @Player.State Int) {

        }

        override fun onPlayerError(error: PlaybackException) {

        }

        override fun onTracksChanged(tracks: Tracks) {

        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)




        updateTrackSelectorParameters()


        outState.putBundle(
            KEY_TRACK_SELECTION_PARAMETERS,
            trackSelectionParameters!!.toBundle()
        )
        outState.putBoolean(
            KEY_AUTO_PLAY,
            startAutoPlay
        )
        outState.putInt(
            KEY_ITEM_INDEX,
            startItemIndex
        )
        outState.putLong(
            KEY_POSITION,
            startPosition
        )
        // saveServerSideAdsLoaderState(outState)
    }


    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    companion object {
        var URL =
            "https://dxnqhjm6zg0n4.cloudfront.net/file_library/videos/vod_non_drm_ios/74762/active_forum_-_final_videos/1655192575290_184433_VOD.m3u8"
    }


}