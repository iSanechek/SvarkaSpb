package com.isanechek.myapplication.data.models

import android.view.View

sealed class ItemClickCallback<out T: Any> {
    data class ItemClick<out T: Any>(val view: View, val data: T, val position: Int): ItemClickCallback<T>()
    data class ItemLongClick<out T: Any>(val data: T, val position: Int): ItemClickCallback<T>()
}