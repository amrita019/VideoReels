package com.amrita.reels

import android.app.Application
import com.amrita.reels.di.DaggerNetworkComponent
import com.amrita.reels.di.NetworkComponent
import com.amrita.reels.di.NetworkModule

class ReelsApplication: Application() {

    private lateinit var networkComponent: NetworkComponent
    override fun onCreate() {
        super.onCreate()
        networkComponent = DaggerNetworkComponent.builder().networkModule(NetworkModule()).build()
    }

    fun getNetworkComponent(): NetworkComponent {
        return networkComponent
    }
}