package com.isanechek.myapplication.data.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VkMarketItemDetailRequest(
    itemIds: String,
    extended: Int
): VKRequest<JSONObject>("market.getById") {

    init {
        addParam("item_ids", itemIds)
        addParam("extended", extended)
    }

    override fun parse(r: JSONObject): JSONObject = r
}