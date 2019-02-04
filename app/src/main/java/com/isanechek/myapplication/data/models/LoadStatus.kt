package com.isanechek.myapplication.data.models

sealed class LoadStatus {
    sealed class Error {
        data class NetworkError(val errorCode: Int) : Error()
        data class WorkerError(val messageError: String) : Error()
        object UnknownError : Error()
    }
    object Loading : LoadStatus()
    object Done : LoadStatus()
    data class Fail(val error: Error): LoadStatus()
}