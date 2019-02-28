package com.isanechek.myapplication

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.isanechek.myapplication.di.appModule
import com.isanechek.myapplication.di.dataModule
import com.isanechek.myapplication.di.utilsModule
import com.vk.api.sdk.VK
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import org.koin.android.ext.android.startKoin

class App : Application() {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }


    override fun onCreate() {
        super.onCreate()

        val yandexMetricaConfig = YandexMetricaConfig
            .newConfigBuilder("7d379411-ba8a-4cff-ac83-cc1cb4a28e50")
            .withAppVersion(BuildConfig.VERSION_NAME)
            .build()
        YandexMetrica.activate(applicationContext, yandexMetricaConfig)
        YandexMetrica.enableActivityAutoTracking(this)

        VK.initialize(this)


        startKoin(
            this, listOf(
                appModule,
                dataModule,
                utilsModule
            )
        )
    }
}