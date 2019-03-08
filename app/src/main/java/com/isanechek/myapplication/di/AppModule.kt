package com.isanechek.myapplication.di

import com.isanechek.myapplication.screens.dashboard.DashboardViewModel
import com.isanechek.myapplication.screens.gallery.PhotosViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    viewModel {
        PhotosViewModel()
    }

    viewModel {
        DashboardViewModel(
            get(),
            get(),
            get(),
            get()
        )
    }
}