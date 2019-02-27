package com.isanechek.myapplication.screens.gallery

import android.os.Handler
import android.os.Looper
import androidx.paging.PagedList
import com.isanechek.myapplication.data.Photo
import com.isanechek.myapplication.data.PhotosDataSource
import com.isanechek.myapplication.screens.base.BaseViewModel
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PhotosViewModel : BaseViewModel(), KoinComponent {

    private val dataSource: PhotosDataSource by inject()

    private val mainHandler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val ui = Executor {
        mainHandler.post(it)
    }

    private val pageConfig = PagedList.Config.Builder()
        .setPageSize(10)
        .setPrefetchDistance(4)
        .build()

    val data by lazy {
        PagedList.Builder<Int, Photo>(dataSource, pageConfig)
            .setNotifyExecutor(ui)
            .setFetchExecutor(Executors.newCachedThreadPool())
            .build()
    }

    val progress = dataSource.progress

}