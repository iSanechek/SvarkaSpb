package com.isanechek.myapplication.utils

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.isanechek.myapplication.utils.glide.OkHttpGlideUrlLoader
import com.isanechek.myapplication.utils.glide.ProgressInterceptor
import okhttp3.OkHttpClient
import java.io.InputStream

@GlideModule
class CoverGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.setMemoryCache(
            LruResourceCache(
                MemorySizeCalculator.Builder(context)
                    .setMemoryCacheScreens(2f)
                    .build()
                    .memoryCacheSize
                    .toLong()
            )
        )
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(ProgressInterceptor())
        val okClient = builder.build()
        registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpGlideUrlLoader.Factory(okClient))
    }
}