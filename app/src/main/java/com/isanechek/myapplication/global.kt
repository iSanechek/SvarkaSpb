package com.isanechek.myapplication

import android.view.View

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