package com.isanechek.myapplication

import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.isanechek.myapplication.utils.GlideApp
import com.isanechek.myapplication.utils.glide.CustomGlideDrawableImageViewTarget
import com.isanechek.myapplication.utils.glide.CustomProgressListener
import com.isanechek.myapplication.utils.glide.ProgressInterceptor
import kotlinx.coroutines.Deferred

typealias _layout = R.layout
typealias _id = R.id
typealias _drawable = R.drawable

fun View.onClick(function: () -> Unit) {
    setOnClickListener {
        function()
    }
}

fun View.setVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun String.Companion.empty() = ""

fun ImageView.load(url: String, progress: ProgressBar? = null) {
    GlideApp.with(this.context).load(url).into(this)
//    when (progress) {
//        null -> GlideApp.with(this.context).load(url).into(this)
//        else -> {
//
//        }
//    }
}

inline fun delay(milliseconds: Long, crossinline action: () -> Unit) {
    Handler().postDelayed({
        action()
    }, milliseconds)
}

suspend fun <T> retryDeferredWithDelay(
    deferred: () -> Deferred<T>,
    tries: Int = 3,
    timeDelay: Long = 1000L
) : T {
    for (i in 1..tries) {
        try {
            return deferred().await()
        } catch (e: Exception) {
            if (i < tries) kotlinx.coroutines.delay(timeDelay) else throw e
        }
    }
    throw UnsupportedOperationException()
}