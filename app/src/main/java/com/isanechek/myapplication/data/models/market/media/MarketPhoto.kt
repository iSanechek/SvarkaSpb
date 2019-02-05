package com.isanechek.myapplication.data.models.market.media

import com.isanechek.myapplication.data.models.Size

data class MarketPhoto(
    val id: Int,
    val albumId: Int,
    val ownerId: Int,
    val userId: Int,
    val text: String,
    val date: Long,
    val sizeCovers: List<Size>
)