package com.isanechek.myapplication.data.models.market

data class MarketItem(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val description: String,
    val price: MarketItemPrice,
    val category: MarketItemCategory,
    val date: Long,
    val coverUrl: String,
    val availability: Int,
    val favorite: Boolean
)