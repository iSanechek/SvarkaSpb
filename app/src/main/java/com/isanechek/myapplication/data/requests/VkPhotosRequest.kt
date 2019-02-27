package com.isanechek.myapplication.data.requests

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class VkPhotosRequest(
    requestCount: Int,
    requestOffset: Int
) : VKRequest<JSONObject>("photos.getAll") {

    init {
        addParam("owner_id", -125640924)
        addParam("extended", 0)
        addParam("offset", requestOffset)
        addParam("count", requestCount)
        addParam("photo_sizes", 1)
        addParam("skip_hidden", 1)
    }

    override fun parse(r: JSONObject): JSONObject = r
}