package com.isanechek.myapplication.di

import com.isanechek.myapplication.screens.dashboard.DashboardViewModel
import com.isanechek.myapplication.screens.gallery.PhotosViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        PhotosViewModel(androidApplication(), get())
    }

    viewModel {
        DashboardViewModel(
            androidApplication(),
            get(),
            get(),
            get(),
            get()
        )
    }
}