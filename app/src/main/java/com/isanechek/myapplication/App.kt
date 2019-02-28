package com.isanechek.myapplication

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.isanechek.myapplication.di.appModule
import com.isanechek.myapplication.di.dataModule
import com.isanechek.myapplication.di.utilsModule
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import org.koin.android.ext.android.startKoin

class App : Application() {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }


    override fun onCreate() {
        super.onCreate()

        VK.initialize(this)


        startKoin(this, listOf(
            appModule,
            dataModule,
            utilsModule
        ))
    }
}