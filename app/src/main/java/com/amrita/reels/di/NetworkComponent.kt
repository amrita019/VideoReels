package com.amrita.reels.di

import com.amrita.reels.viewmodel.ViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface NetworkComponent {

    fun inject(viewModel: ViewModel)
}