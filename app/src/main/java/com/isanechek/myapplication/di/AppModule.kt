package com.isanechek.myapplication.di

import com.isanechek.myapplication.screens.shop.ShopViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    viewModel {
        ShopViewModel(
            get(),
            get()
        )
    }
}