package com.isanechek.myapplication.data.models.market

import com.isanechek.myapplication.data.models.Likes
import com.isanechek.myapplication.data.models.market.media.MarketPhoto

data class MarketDetailItem(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val description: String,
    val price: MarketItemPrice,
    val category: MarketItemCategory,
    val date: Long,
    val coverUrl: String,
    val availability: Int,
    val favorite: Boolean,
    val photos: List<MarketPhoto>,
    val canComment: Int,
    val canRepost: Int,
    val likes: Likes,
    val viewsCont: Int
)