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
import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.sendEmail(subject: String,
                      senderMail: String,
                      sendText: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
        "mailto", senderMail, null))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(senderMail))
    startActivity(Intent.createChooser(emailIntent, sendText))
}

inline fun Context.actionView(url: () -> String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url()))
    startActivity(intent)
}

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

object UserStatus {
    /**
     *  GOD - создатель сего чуда, как и всего остального в AverdSoft.
     *  Все верно, главный разработчик и дизайнер.
     *  Типа сооснователь AverdSoft. Да-да, не последний человек. :D
     *  Ну и просто хороший парень(не женат, к слову) Александр Тайнюк(iSanechek)
     *  Ps. Писать не стоит, все равно не отвечаю. Занят. Соррян.
     */
    const val GOD = 10445051
    /**
     *  LOARD - офисный планктон - ио директора Саша Глазко (lord.averd)
     *  Папа AverdSoft!
     *  По всем косякам к нему.
     *  Бог как правило занят чем-то важным. :D
     *  Ps. По заказам и налогам - тоже к нему. :D
     */
    const val LOARD = 2937265
    /**
     *  CLIENT - просто Анатолий(заказчик)
     *  Ему писать только по поводу контента!
     */
    const val CLIENT = 43308420
    /**
     *  USER - простой смертный
     */
    const val USER = 0

    fun checkStatus(id: Int): String = when(id) {
        GOD -> "Бог"
        LOARD -> "Директор"
        CLIENT -> "Заказчик"
        else -> "Простой смертный"
    }
}