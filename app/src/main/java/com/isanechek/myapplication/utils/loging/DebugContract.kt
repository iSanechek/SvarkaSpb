package com.isanechek.myapplication.utils.loging

interface DebugContract {
    fun log(msg: String?)
    fun event(event: String?)
    fun event(tag: String, event: String?)
    fun event(tag: String, events: Map<String, Any>)
    fun sendStackTrace(tag: String, exception: Throwable?)
}