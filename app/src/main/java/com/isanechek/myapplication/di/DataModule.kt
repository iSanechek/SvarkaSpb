package com.isanechek.myapplication.di

import android.content.Context
import android.content.SharedPreferences
import com.isanechek.myapplication.BuildConfig
import com.isanechek.myapplication.data.DatabaseHandler
import com.isanechek.myapplication.data.DbContract
import com.isanechek.myapplication.data.PhotosDataSource
import com.isanechek.myapplication.data.remote.VkApiService
import com.isanechek.myapplication.utils.CoroutineCallAdapterFactory
import com.isanechek.myapplication.utils.PrefManager
import com.isanechek.myapplication.utils.PrefManagerImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private fun createWallpapersService(client: OkHttpClient) = Retrofit.Builder()
    .baseUrl("https://api.vk.com/method/")
    .client(client)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()
    .create(VkApiService::class.java)

private fun createOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
    })
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

val dataModule = module {

    single {
        createOkHttpClient()
    }

    single {
        createWallpapersService(
            get()
        )
    }

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

    single<DbContract> {
        DatabaseHandler(
            androidContext().applicationContext
        )
    }

    single {
        PhotosDataSource(
            androidContext(),
            get(),
            get(),
            get()
        )
    }
}