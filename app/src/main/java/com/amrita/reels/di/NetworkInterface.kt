package com.amrita.reels.di

import com.amrita.reels.model.ResponseModel
import retrofit2.Call
import retrofit2.http.GET

interface NetworkInterface {

    @GET("search?key=AIzaSyA4Vqpf_y2u2t-z7B5hz0jarfQbx50tRUM&channelId=UCWXcdemE-1OGDhaSAPAw-4w&part=snippet,id&order=date&maxResults=20")
    fun getAllVideos(): Call<ResponseModel>?

}