package com.isanechek.myapplication.screens.dashboard

import android.app.Application
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.isanechek.myapplication.SERVICE_KEY
import com.isanechek.myapplication._text
import com.isanechek.myapplication.data.DbContract
import com.isanechek.myapplication.data.Photo
import com.isanechek.myapplication.data.models.LoadStatus
import com.isanechek.myapplication.data.remote.VkApiService
import com.isanechek.myapplication.empty
import com.isanechek.myapplication.retryDeferredWithDelay
import com.isanechek.myapplication.utils.NetworkUtils
import com.isanechek.myapplication.utils.PrefManager
import com.isanechek.myapplication.utils.loging.DebugContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class DashboardViewModel(
    application: Application,
    private val debug: DebugContract,
    private val apiVk: VkApiService,
    private val db: DbContract,
    private val pref: PrefManager
) : AndroidViewModel(application) {

    private val context: Context = getApplication()

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    val loadStatus = MutableLiveData<LoadStatus>()
    val liveData = MutableLiveData<List<Photo>>()

    fun loadPhotos() {
        viewModelScope.launch(Dispatchers.Default) {
            if (NetworkUtils.isConnected(context)) {
                loadAndSaveData()
            } else {
                showToast(_text.no_network_error_msg)
            }
        }
    }

    fun showToast(@StringRes textId: Int) {
        _toast.postValue(context.getString(textId))
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
                versionApi = "5.92"
            )
        }
    )

    private suspend fun loadAndSaveData() = withContext(Dispatchers.IO) {

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
                if (timex > TimeUnit.HOURS.toMillis(1)) {
                    loadFromNetwork = true
                    debug.log("save time $now")
                    pref.lastUpdateTime = now
                }
            }
        }

        if (loadFromNetwork) {
            loadStatus.postValue(LoadStatus.Loading)
            val data = loadData().string()
            val temp = mutableListOf<Photo>()
            try {
                val jo = JSONObject(data)
                val items = jo.getJSONObject("response").getJSONArray("items")
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val urls = getUrls(item.getJSONArray("sizes"))
                    if (urls.first.isEmpty()) {
                        break
                    }

                    temp.add(
                        Photo(
                            id = item.getInt("id"),
                            ownerId = item.getInt("owner_id"),
                            albumsId = 0,
                            smallUrl = urls.first,
                            fullUrl = urls.second
                        )
                    )
                }

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
                loadStatus.postValue(LoadStatus.Fail(LoadStatus.Error.UnknownError))
            }
        }
    }

    private fun getUrls(ja: JSONArray): Pair<String, String> {
        var url = String.empty()
        val imageSize = arrayOf("w", "z", "y", "q", "p", "o", "x", "m", "s")
        val temp = Array<JSONObject>(ja.length()) { i ->
            ja.getJSONObject(i)
        }
        for (s in imageSize) {
            val obj = temp.firstOrNull { i -> i.getString("type") == s  }
            if (obj != null) {
                url = obj.getString("url")
                break
            }
        }
        return Pair(url, url)
    }
}