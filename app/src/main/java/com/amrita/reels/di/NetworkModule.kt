package com.amrita.reels.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    /**
     * API Used
     * https://www.googleapis.com/youtube/v3/search?key=AIzaSyA4Vqpf_y2u2t-z7B5hz0jarfQbx50tRUM&channelId=UCWXcdemE-1OGDhaSAPAw-4w&part=snippet,id&order=date&maxResults=20*/

    private val baseUrl = "https://www.googleapis.com/youtube/v3/"

    @Singleton
    @Provides
    fun getRetrofitInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun getNetworkInterface(retrofit: Retrofit): NetworkInterface{
        return retrofit.create(NetworkInterface::class.java)
    }

}