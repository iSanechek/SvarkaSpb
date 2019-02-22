package com.isanechek.myapplication.utils.glide

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition

class CustomGlideDrawableImageViewTarget(view: ImageView,
                                         private val title: TextView,
                                         private val description: TextView,
                                         private val username: String,
                                         private val roundProgressBar: ProgressBar,
                                         private val url: String
) : DrawableImageViewTarget(view) {

    override fun onLoadStarted(placeholder: Drawable?) {
        super.onLoadStarted(placeholder)
        roundProgressBar.visibility = View.VISIBLE
        roundProgressBar.progress = 1
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        roundProgressBar.visibility = View.GONE
        ProgressInterceptor.removeListener(url)
        title.text = "Ошибка при загрузке данных!"
        super.onLoadFailed(errorDrawable)
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        super.onResourceReady(resource, transition)
        roundProgressBar.visibility = View.GONE
        ProgressInterceptor.removeListener(url)
        title.text = "Добро пожаловать"
        description.text = username
    }
}