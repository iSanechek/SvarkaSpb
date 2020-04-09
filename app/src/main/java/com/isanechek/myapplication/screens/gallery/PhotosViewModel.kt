package com.isanechek.myapplication.screens.gallery

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.paging.PagedList
import com.isanechek.myapplication.data.Photo
import com.isanechek.myapplication.data.PhotosDataSource
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PhotosViewModel(application: Application, private val dataSource: PhotosDataSource) :
    AndroidViewModel(application) {

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
        PagedList.Builder(dataSource, pageConfig)
            .setNotifyExecutor(ui)
            .setFetchExecutor(Executors.newCachedThreadPool())
            .build()
    }

    val progress = dataSource.progress

}