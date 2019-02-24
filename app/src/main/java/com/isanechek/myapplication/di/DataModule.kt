package com.isanechek.myapplication.di

import android.content.Context
import android.content.SharedPreferences
import com.isanechek.myapplication.utils.PrefManager
import com.isanechek.myapplication.utils.PrefManagerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val dataModule = module {

    single {
        androidContext()
            .applicationContext
            .getSharedPreferences("svarka", Context.MODE_PRIVATE)
    } bind (SharedPreferences::class)


    single<PrefManager> {
        PrefManagerImpl(
            get()
        )
    }
}