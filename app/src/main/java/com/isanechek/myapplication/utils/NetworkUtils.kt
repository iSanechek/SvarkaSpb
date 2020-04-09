package com.isanechek.myapplication.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val an = cm.activeNetworkInfo
        return an?.isConnectedOrConnecting ?: false
    }
}