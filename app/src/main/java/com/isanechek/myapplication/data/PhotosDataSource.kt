package com.isanechek.myapplication.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.isanechek.myapplication.SERVICE_KEY
import com.isanechek.myapplication.data.models.LoadStatus
import com.isanechek.myapplication.data.remote.VkApiService
import com.isanechek.myapplication.data.requests.VkPhotosRequest
import com.isanechek.myapplication.empty
import com.isanechek.myapplication.retryDeferredWithDelay
import com.isanechek.myapplication.utils.loging.DebugContract
import com.vk.api.sdk.VK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class PhotosDataSource(private val debug: DebugContract,
                       private val db: DbContract,
                       private val api: VkApiService) : PositionalDataSource<Photo>() {

    val progress = MutableLiveData<LoadStatus>()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Photo>) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = when {
                VK.isLoggedIn() -> loadPhotos(params.requestedLoadSize, params.requestedStartPosition)
                else -> loadPhotosNotAuth(params.requestedLoadSize, params.requestedStartPosition)
            }
            if (result.second.isNotEmpty()) {
                db.savePhotos(result.second)
            }
            callback.onResult(result.second, params.requestedStartPosition, result.first)
            progress.postValue(LoadStatus.Done)
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Photo>) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = when {
                VK.isLoggedIn() -> loadPhotos(params.loadSize, params.startPosition)
                else -> loadPhotosNotAuth(params.loadSize, params.startPosition)
            }
            callback.onResult(result.second)
            progress.postValue(LoadStatus.Done)
        }
    }

    private suspend fun loadData(limit: Int, offset: Int) = retryDeferredWithDelay(
        deferred = {
            api.getPhotos(
                ownerId = "-125640924",
                album_id = "wall",
                accessToken = SERVICE_KEY,
                count = limit,
                offset = offset,
                needPhotoSizes = 1,
                extended = 0,
                skipHidden = 1,
                versionApi = "5.92")
        }
    )


    private suspend fun loadPhotosNotAuth(limit: Int, offset: Int): Pair<Int, List<Photo>> {
        progress.postValue(LoadStatus.Loading)
        val temp = mutableListOf<Photo>()
        val data = loadData(limit, offset).string()
        try {
            val jo = JSONObject(data)
            val items = jo.getJSONObject("response").getJSONArray("items")
            Log.e("Hyi", "size ${items.length()}")
            for (i in 0 until items.length()) {
                val item = items.getJSONObject(i)
                val urls = getUrls(item.getJSONArray("sizes"))
                temp.add(Photo(
                    id = item.getInt("id"),
                    ownerId = item.getInt("owner_id"),
                    albumsId = 0,
                    smallUrl = urls.first,
                    fullUrl = urls.second
                ))
            }
        } catch (e: Exception) {
            debug.log("No auth load data error ${e.message}")
            progress.postValue(LoadStatus.Fail(LoadStatus.Error.UnknownError))
        }
        return Pair(temp.size, temp)
    }

    private fun loadPhotos(limit: Int, offset: Int): Pair<Int, List<Photo>> {
        progress.postValue(LoadStatus.Loading)
        val temp = mutableListOf<Photo>()
        val jo = VK.executeSync(VkPhotosRequest(limit, offset))
        val response = jo.getJSONObject("response")
        val items = response.getJSONArray("items")
        val totalCount = response.getInt("count")

        for (i in 0 until items.length()) {
            val item = items.getJSONObject(i)
//            debug.log(item.toString())
            val urls = getUrls(item.getJSONArray("sizes"), true)
            temp.add(Photo(
                id = item.getInt("id"),
                ownerId = item.getInt("owner_id"),
                albumsId = item.getInt("album_id"),
                smallUrl = urls.first,
                fullUrl = urls.second
            ))
        }

        return Pair(totalCount, temp)
    }

    private fun getUrls(ja: JSONArray, auth: Boolean = false) : Pair<String, String> {
        var full = String.empty()
        var small = String.empty()

        for (i in 0 until ja.length()) {
            val item = ja.getJSONObject(i)
            val type = item.getString("type")
//            val tag = if (auth) "url" else "src"
            val tag = "url"
            when (type) {
                FULL_TYPE -> full = item.getString(tag).replaceAfter(".jpg", "")
                SMALL_TYPE -> small = item.getString(tag).replaceAfter(".jpg", "")
            }
        }

        return Pair(small, full)
    }

    companion object {
        private const val SMALL_TYPE = "z"
        private const val FULL_TYPE = "w"
    }
}