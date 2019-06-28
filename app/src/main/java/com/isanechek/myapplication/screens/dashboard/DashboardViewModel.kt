package com.isanechek.myapplication.screens.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.isanechek.myapplication.SERVICE_KEY
import com.isanechek.myapplication.data.DbContract
import com.isanechek.myapplication.data.Photo
import com.isanechek.myapplication.data.models.LoadStatus
import com.isanechek.myapplication.data.remote.VkApiService
import com.isanechek.myapplication.empty
import com.isanechek.myapplication.retryDeferredWithDelay
import com.isanechek.myapplication.screens.base.BaseViewModel
import com.isanechek.myapplication.utils.PrefManager
import com.isanechek.myapplication.utils.loging.DebugContract
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class DashboardViewModel(private val debug: DebugContract,
                         private val apiVk: VkApiService,
                         private val db: DbContract,
                         private val pref: PrefManager
) : BaseViewModel() {

    val loadStatus = MutableLiveData<LoadStatus>()
    val liveData = MutableLiveData<List<Photo>>()

    fun loadPhotos() {
        addJob(job = launch(context = coroutineContext) {
            loadAndSaveData()
        }, key = "load_photos")
    }

    private suspend fun loadData() = retryDeferredWithDelay(
       deferred = {
           apiVk.getPhotos(
               ownerId = "-125640924",
               album_id = "wall",
               accessToken = SERVICE_KEY,
               count = 10,
               offset = 0,
               needPhotoSizes = 1,
               extended = 0,
               skipHidden = 1,
               versionApi = "5.92")
       }
    )

    private suspend fun loadAndSaveData() {

        var loadFromNetwork = false
        var dbIsEmpty = false
        val fromDb = db.getPhotos()
        when {
            fromDb.isEmpty() -> {
                debug.log("Db is empty")
                dbIsEmpty = true
                loadFromNetwork = true
            }
            else -> {
                liveData.postValue(fromDb)
                debug.log("Db is not empty ${fromDb.size}")
                val now = System.currentTimeMillis()
                val last = pref.lastUpdateTime
                val timex = now - last
                debug.log("time last $last")
                debug.log("time timex $timex")
                if (timex > 28800000) {
                    loadFromNetwork = true
                    debug.log("save time $now")
                    pref.lastUpdateTime = now
                }
            }
        }

        debug.log("Load from network $loadFromNetwork")
        if (loadFromNetwork) {
            loadStatus.postValue(LoadStatus.Loading)
            val data = loadData().string()
            debug.log("Data from network $data")
            val temp = mutableListOf<Photo>()
            try {
                val jo = JSONObject(data)
                val items = jo.getJSONObject("response").getJSONArray("items")
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

                debug.log("Temp size ${temp.size}")
                if (temp.isNotEmpty()) {
                    loadStatus.postValue(LoadStatus.Done)
                    liveData.postValue(temp)
                    when {
                        dbIsEmpty -> db.savePhotos(temp)
                        else -> {
                            db.clearPhotos()
                            db.savePhotos(temp)
                        }
                    }
                }

            } catch (e: Exception) {
                debug.log("Error ${e.message}")
                Log.e("Hyi", "Error ${e.message}")
                loadStatus.postValue(LoadStatus.Fail(LoadStatus.Error.UnknownError))
            }
        }
    }

    private fun getUrls(ja: JSONArray) : Pair<String, String> {
        var full = String.empty()
        var small = String.empty()

        for (i in 0 until ja.length()) {
            val item = ja.getJSONObject(i)
            val type = item.getString("type")
            when (type) {
                FULL_TYPE -> full = item.getString("url").replaceAfter(".jpg", "")
                SMALL_TYPE -> small = item.getString("url").replaceAfter(".jpg", "")
            }
        }
        return Pair(small, full)
    }

    companion object {
        private const val SMALL_TYPE = "z"
        private const val FULL_TYPE = "w"
    }
}