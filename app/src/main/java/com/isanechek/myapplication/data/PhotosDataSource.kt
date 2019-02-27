package com.isanechek.myapplication.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.isanechek.myapplication.data.models.LoadStatus
import com.isanechek.myapplication.data.requests.VkPhotosRequest
import com.isanechek.myapplication.empty
import com.isanechek.myapplication.utils.loging.DebugContract
import com.vk.api.sdk.VK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray

class PhotosDataSource(private val debug: DebugContract,
                       private val db: DbContract) : PositionalDataSource<Photo>() {

    val progress = MutableLiveData<LoadStatus>()

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Photo>) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = loadPhotos(params.requestedLoadSize, params.requestedStartPosition)
            if (result.second.isNotEmpty()) {
                db.savePhotos(result.second)
            }
            callback.onResult(result.second, params.requestedStartPosition, result.first)
            progress.postValue(LoadStatus.Done)
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Photo>) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = loadPhotos(params.loadSize, params.startPosition)
            callback.onResult(result.second)
            progress.postValue(LoadStatus.Done)
        }
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
            val urls = getUrls(item.getJSONArray("sizes"))
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

    private fun getUrls(ja: JSONArray) : Pair<String, String> {
        var full = String.empty()
        var small = String.empty()

        for (i in 0 until ja.length()) {
            val item = ja.getJSONObject(i)
            val type = item.getString("type")
            when (type) {
                FULL_TYPE -> full = item.getString("url")
                SMALL_TYPE -> small = item.getString("url")
            }
        }

        return Pair(small, full)
    }

    companion object {
        private const val SMALL_TYPE = "z"
        private const val FULL_TYPE = "w"
    }
}