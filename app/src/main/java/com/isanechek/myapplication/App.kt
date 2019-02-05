package com.isanechek.myapplication

import android.app.Application
import com.isanechek.myapplication.di.dataModule
import com.isanechek.myapplication.di.utilsModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(
            dataModule,
            utilsModule
        ))
    }
}