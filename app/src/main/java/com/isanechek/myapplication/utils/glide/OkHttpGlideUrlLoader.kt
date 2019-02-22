package com.isanechek.myapplication.utils.glide

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import okhttp3.OkHttpClient
import java.io.InputStream

class OkHttpGlideUrlLoader(private val okHttpClient: OkHttpClient) : ModelLoader<GlideUrl, InputStream> {

    override fun buildLoadData(
        model: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream>? = ModelLoader.LoadData(model, OkHttpFetcher(okHttpClient, model))

    override fun handles(model: GlideUrl): Boolean = true

    class Factory (private val client: OkHttpClient): ModelLoaderFactory<GlideUrl, InputStream> {

        private val okHttpClient: OkHttpClient
            @Synchronized get() {
                return client
            }

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, InputStream> =
            OkHttpGlideUrlLoader(okHttpClient)

        override fun teardown() {}
    }
}