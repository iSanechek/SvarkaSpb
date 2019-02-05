package com.isanechek.myapplication.utils.loging

import android.util.Log
import com.isanechek.myapplication.BuildConfig

class DebugImpl : DebugContract {

    private val isDebug
        get() = BuildConfig.DEBUG

    override fun event(event: String?) {

    }

    override fun event(tag: String, event: String?) {
    }

    override fun event(tag: String, events: Map<String, Any>) {
    }

    override fun sendStackTrace(tag: String, exception: Throwable?) {
        if (isDebug) {
            Log.e("Svarka", tag, exception)
        }
    }

    override fun log(msg: String?) {
        if (isDebug) {
            Log.d("Svarka", msg)
        }
    }

}