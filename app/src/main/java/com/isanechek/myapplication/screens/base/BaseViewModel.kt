package com.isanechek.myapplication.screens.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.isanechek.myapplication.utils.SingleLiveEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val jobsCache = hashMapOf<String, Job>()
    private val parentJob = SupervisorJob()

    val showToast = SingleLiveEvent<String>()
    val showEmpty = SingleLiveEvent<Boolean>()

    private val showToolbarProgress = SingleLiveEvent<Boolean>()
    val toolbarProgressState: LiveData<Boolean>
        get() = showToolbarProgress

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    override fun onCleared() {
        if(jobsCache.isNotEmpty()) jobsCache.clear()
        coroutineContext.cancel()
        super.onCleared()
    }

    protected fun addJob(job: Job, key: String) = jobsCache.put(key, job)?.cancel()
    protected fun cancelJob(key: String) = jobsCache[key]?.cancel()

}