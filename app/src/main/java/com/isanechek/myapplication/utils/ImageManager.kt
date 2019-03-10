package com.isanechek.myapplication.utils

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bannerlayout.ImageLoaderManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.isanechek.myapplication._drawable
import com.isanechek.myapplication.data.models.Banner
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class ImageManager : ImageLoaderManager<Banner> {

    private val requestOptions: RequestOptions = RequestOptions().centerCrop()

    override fun display(container: ViewGroup, model: Banner): View {
        val iv = ImageView(container.context)

        GlideApp.with(iv.context)
            .applyDefaultRequestOptions(requestOptions)
            .load(model.bannerUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(8, 0)))
            .placeholder(_drawable.ic_photo_black_96dp)
            .error(_drawable.ic_broken_image_black_96dp)
            .fallback(_drawable.ic_photo_black_96dp)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)

        return iv
    }


}