package com.isanechek.myapplication.screens.gallery

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.isanechek.myapplication.utils.glide.ProgressInterceptor

class PhotoDetailGlideDrawableImageViewTarget(image: ImageView,
                                              private val roundProgressBar: ProgressBar,
                                              private val url: String,
                                              private val callback: () -> Unit
) : DrawableImageViewTarget(image) {

    override fun onLoadStarted(placeholder: Drawable?) {
        super.onLoadStarted(placeholder)
        roundProgressBar.visibility = View.VISIBLE
        roundProgressBar.progress = 1
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        roundProgressBar.visibility = View.GONE
        ProgressInterceptor.removeListener(url)
        callback()
        super.onLoadFailed(errorDrawable)
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(resource, transition)
        roundProgressBar.visibility = View.GONE
        ProgressInterceptor.removeListener(url)
    }
}