package com.isanechek.myapplication.data.parsers

import com.isanechek.myapplication.data.models.VKUser
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import org.json.JSONException
import org.json.JSONObject

class ResponseUserParser : VKApiResponseParser<List<VKUser>> {
    override fun parse(response: String?): List<VKUser> = try {
        val ja = JSONObject(response).getJSONArray("response")
        val users = ArrayList<VKUser>(ja.length())
        for (i in 0 until ja.length()) {
            val user = VKUser.parse(ja.getJSONObject(i))
            users.add(user)
        }
        users
    } catch (ex: JSONException) {
        throw VKApiIllegalResponseException(ex)
    }
}