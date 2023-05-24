package com.erenalparslan.octopusclone


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


class Play : Fragment() {

    private var fragmentPlayBinding: FragmentPlayBinding? = null
    lateinit var exoPlayer: ExoPlayer
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private var playWhenReady = true
    private var player: ExoPlayer? = null
    private var playerView: StyledPlayerView? = null
    private var fullscreenButton: ImageView? = null

    private var exoPlay: ImageView? = null
    private var exoPause: ImageView? = null
    private var exoRestart: ImageView? = null
    private var volumeBtn: ImageView? = null
    private var ivVideoQuality: ImageView? = null


    var isShowingTrackSelectionDialog: Boolean = false
    private var trackSelectionParameters: TrackSelectionParameters? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exoPlayer = ExoPlayer.Builder(requireContext())
            //   .setTrackSelector(trackSelector)
            .build()
        if (savedInstanceState != null) {
            /*   trackSelectionParameters = TrackSelectionParameters.fromBundle(
                   savedInstanceState.getBundle(KEY_TRACK_SELECTION_PARAMETERS)!!
               )*/
            currentWindow = savedInstanceState.getInt("current_window")
            playbackPosition = savedInstanceState.getLong("playback_position")

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_play, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPlayBinding.bind(view)
        fragmentPlayBinding = binding


        preparePlayer()
    }

    fun playerSetter(player: ExoPlayer, mediaItem: MediaItem) {
        player.setMediaItem(mediaItem, playbackPosition)
        player.prepare()
        player.play()
    }


    private fun preparePlayer() {

        playerView = requireActivity().findViewById(R.id.playerView)
        fullscreenButton = playerView?.findViewById(R.id.bt_full)
        exoPlay = playerView?.findViewById(R.id.exo_play)
        exoPause = playerView?.findViewById(R.id.exo_pause)
        exoRestart = playerView?.findViewById(R.id.exo_restart)
        volumeBtn = playerView?.findViewById(R.id.volumebtn)
        ivVideoQuality = playerView?.findViewById(R.id.ivVideoQuality)


        fragmentPlayBinding?.playerView?.player = exoPlayer
        exoPlayer!!.addListener(PlayerEventListener())

        val mediaItem = MediaItem.fromUri(URL)

        playerSetter(exoPlayer!!, mediaItem)




        ivVideoQuality?.setOnClickListener {

            isShowingTrackSelectionDialog = true
            val trackSelectionDialog = TrackSelectionDialog.createForPlayer(
                exoPlayer
            )  /* onDismissListener= */
            { dismissedDialog -> isShowingTrackSelectionDialog = false }
            trackSelectionDialog.show(requireActivity().supportFragmentManager,  /* tag= */null)
            /* }*/
        }

        exoPause?.setOnClickListener {

            exoPlayer?.playWhenReady = false
            exoPause?.visibility = View.GONE
            exoPlay?.visibility = View.VISIBLE

        }
        exoPlay?.setOnClickListener {
            exoPlayer?.playWhenReady = true
            exoPlay?.visibility = View.GONE
            exoPause?.visibility = View.VISIBLE
        }
        volumeBtn?.setOnClickListener {
            if (exoPlayer?.isDeviceMuted == false) {
                exoPlayer?.isDeviceMuted = true
                volumeBtn?.setImageResource(R.drawable.ic_volume_off)
            } else {
                exoPlayer?.isDeviceMuted = false
                volumeBtn?.setImageResource(R.drawable.ic_volume_up)
            }

        }

    }


    private fun releasePlayer() {
        exoPlayer?.let { player ->
            playbackPosition = player.currentPosition
            playWhenReady = player.playWhenReady
            player.release()
            // exoPlayer = null
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



        outState.putInt("current_window", exoPlayer!!.currentWindowIndex)
        outState.putLong("playback_position", exoPlayer!!.currentPosition)


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