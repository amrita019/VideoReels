package com.amrita.reels.view

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrita.reels.databinding.ActivityMainBinding
import com.amrita.reels.viewmodel.ViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var viewModel: ViewModel
    private lateinit var mYoutubePlayer: YouTubePlayer
    private var playerState: String = "PAUSED"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setup()
    }

    private fun setup(){
        /** Setting up Recycler View */
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerViewAdapter(recyclerViewItemClick)
        binding.recyclerView.adapter = adapter

        /** Setting up ViewModel */
        viewModel = ViewModelProvider(this).get(ViewModel::class.java )
        viewModel.getLiveData().observe(this
        ) { data ->
            data.let {
                adapter.setData(data?.items)
                adapter.notifyDataSetChanged()
                data?.items?.get(0)?.id?.videoId?.let { it1 -> initializePlayer(it1) }
            }
        }

        /** Making the API call */
        viewModel.getAllClosedPullRequests()

        /** Setting up Player*/
        lifecycle.addObserver(binding.playerView)

    }

    private val recyclerViewItemClick = { id: String ->
        mYoutubePlayer.cueVideo(id, 0f)
        mYoutubePlayer.play()
        updateVideoPlayCount(id)
    }

    private fun initializePlayer(firstVideoId: String){
        /** Initialize Player */
        binding.playerView.enableAutomaticInitialization = false
        binding.playerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                mYoutubePlayer = youTubePlayer
                youTubePlayer.cueVideo(firstVideoId, 0f)
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
                super.onVideoDuration(youTubePlayer, duration)
                viewModel.totalDurationOfVideo = duration
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                viewModel.durationWatched = second
                calculatePercentageWatched()
            }

            override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
                super.onVideoId(youTubePlayer, videoId)
                viewModel.videoWatchedDuration = 0F
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                playerState = if (state == PlayerConstants.PlayerState.PLAYING) {
                    "PLAYING"
                } else {
                    "PAUSED"
                }
                val handler = Handler(Looper.getMainLooper())
                    handler.post(object : Runnable {
                        override fun run() {
                            if (playerState == "PLAYING") {
                                viewModel.videoWatchedDuration += 1
                                calculatePercentageWatched()
                                handler.postDelayed(this, 1000)
                            } else {
                                handler.removeCallbacksAndMessages(null)
                            }
                        }
                    })
            }
        })
    }

    private fun calculatePercentageWatched(){
        var percentage = 0
        try {
            percentage = (((viewModel.videoWatchedDuration / viewModel.totalDurationOfVideo) * 100).roundToInt())
        } catch (e: Exception){
            Log.e("MainActivity", "calculatePercentageWatched: $e")
        }
        binding.tvPercentageWatched.text = "$percentage% watched"
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            binding.playerView.enterFullScreen()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            binding.playerView.exitFullScreen()
        }
    }

    private fun updateVideoPlayCount(videoId: String){
        val count = viewModel.videoInteractionMap?.get(videoId) ?: 0
        viewModel.videoInteractionMap?.put(videoId, count + 1)
    }

}