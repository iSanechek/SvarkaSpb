package com.isanechek.myapplication.data.remote

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApiService {

    @GET("photos.get")
    fun getPhotos(
        @Query("owner_id") ownerId: String,
        @Query("album_id") album_id: String,
        @Query("access_token") accessToken: String,
        @Query("photo_sizes") needPhotoSizes: Int,
        @Query("skip_hidden") skipHidden: Int,
        @Query("extended") extended: Int,
        @Query("count") count: Int,
        @Query("offset") offset: Int,
        @Query("version") versionApi: String
    ): Deferred<ResponseBody>
}