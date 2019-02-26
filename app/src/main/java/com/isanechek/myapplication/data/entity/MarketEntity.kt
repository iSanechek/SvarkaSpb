package com.isanechek.myapplication.data.entity

data class MarketEntity(val id: Int,
                        val ownerId: Int,
                        val title: String,
                        val coverUrl: String,
                        val price: String) {
    companion object {
        const val TABLE_NAME = "market"
        const val COLUMN_ID = "id"
        const val COLUMN_OWNER_ID = "owner_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_COVER_URL = "cover_url"
        const val COLUMN_PRICE = "prigit statce"
    }
}