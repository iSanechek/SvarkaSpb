package com.isanechek.myapplication.utils.glide

import android.util.Log
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


class ProgressResponseBody(url: String, private val responseBody: ResponseBody?) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null
    private var listener: ProgressListener? = ProgressInterceptor.LISTENER_MAP[url]

    override fun contentType(): MediaType? = responseBody?.contentType()

    override fun contentLength(): Long = responseBody?.contentLength() ?: 0L

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(ProgressSource(responseBody!!.source()))
        }
        return bufferedSource
    }

    inner class ProgressSource internal constructor(source: Source) : ForwardingSource(source) {
        private var totalBytesRead: Long = 0
        private var currentProgress: Int = 0
        @Throws(IOException::class)
        override  fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            val fullLength = responseBody!!.contentLength()
            if (bytesRead.toInt() == -1) {
                totalBytesRead = fullLength
            } else {
                totalBytesRead += bytesRead
            }
            val progress = (100f * totalBytesRead / fullLength).toInt()
            if (listener != null && progress != currentProgress) {
                listener!!.onProgress(progress)
            }
            if (listener != null && totalBytesRead == fullLength) {
                listener = null
            }
            currentProgress = progress
            return bytesRead
        }
    }
}