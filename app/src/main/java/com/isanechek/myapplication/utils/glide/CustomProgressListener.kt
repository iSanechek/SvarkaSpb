package com.isanechek.myapplication.utils.glide

import android.widget.ProgressBar

class CustomProgressListener(
    private val progressBar: ProgressBar
) : ProgressListener {

    override fun onProgress(progress: Int) {
        progressBar.progress = progress
    }
}