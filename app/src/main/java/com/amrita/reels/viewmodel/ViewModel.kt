package com.amrita.reels.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.amrita.reels.ReelsApplication
import com.amrita.reels.di.NetworkInterface
import com.amrita.reels.model.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class ViewModel(application: Application): AndroidViewModel(application) {

    @Inject
    lateinit var networkService: NetworkInterface

    private var liveData: MutableLiveData<ResponseModel> = MutableLiveData()
    var isMute: Boolean = false
    var totalDurationOfVideo: Float = 0F
    var durationWatched: Float = 0F
    var videoWatchedDuration: Float = 0F

    var videoInteractionMap: HashMap<String, Int>? = null

    init {
        (application as ReelsApplication).getNetworkComponent().inject(this)
    }

    fun getAllClosedPullRequests(){
        val call: Call<ResponseModel>? = networkService.getAllVideos()
        call?.enqueue(object: Callback<ResponseModel>{
            override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                if(response.isSuccessful){
                    liveData.postValue(response.body())
                } else {
                    liveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                liveData.postValue(null)
            }
        })
    }

    fun getLiveData(): MutableLiveData<ResponseModel>{
        return liveData
    }

}