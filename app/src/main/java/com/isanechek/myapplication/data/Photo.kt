package com.isanechek.myapplication.data

data class Photo(
    val id: Int,
    val ownerId: Int,
    val albumsId: Int,
    val smallUrl: String,
    val fullUrl: String) {

    companion object {
        const val TABLE_NAME = "photo"
        const val TABLE_COLUMN_ID = "id"
        const val TABLE_COLUMN_OWNER_ID = "owner_id"
        const val TABLE_COLUMN_ALBUM_ID = "album_id"
        const val TABLE_COLUMN_FULL_URL = "full_url"
        const val TABLE_COLUMN_SMALL_URL = "small_url"
    }
}