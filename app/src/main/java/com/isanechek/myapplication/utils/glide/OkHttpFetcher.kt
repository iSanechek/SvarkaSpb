package com.isanechek.myapplication.utils.glide


import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.util.ContentLengthInputStream
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.IOException
import java.io.InputStream

class OkHttpFetcher(
    private val client: OkHttpClient,
    private val url: GlideUrl
): DataFetcher<InputStream>{
    private var stream: InputStream? = null
    private var responseBody: ResponseBody? = null
    private var isCancelled: Boolean = false

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val requestBuilder = Request.Builder()
            .url(url.toStringUrl())
        for (headerEntry in url.headers.entries) {
            val key = headerEntry.key
            requestBuilder.addHeader(key, headerEntry.value)
        }
        requestBuilder.addHeader("httplib", "OkHttp")
        val request = requestBuilder.build()
        if (isCancelled) return
        val response = client.newCall(request).execute()
        responseBody = response.body()
        if (!response.isSuccessful || responseBody == null) {
            throw IOException("Request failed with code: " + response.code())
        }
        stream = ContentLengthInputStream.obtain(responseBody!!.byteStream(),
            responseBody!!.contentLength())
        callback.onDataReady(stream)
    }

    override fun getDataClass(): Class<InputStream> = InputStream::class.java

    override fun getDataSource(): DataSource = DataSource.REMOTE

    override fun cleanup() {
        try {
            if (stream != null) {
                stream?.close()
            }
            if (responseBody != null) {
                responseBody?.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun cancel() {
        isCancelled = true
    }

    fun getId(): String = url.cacheKey
}
