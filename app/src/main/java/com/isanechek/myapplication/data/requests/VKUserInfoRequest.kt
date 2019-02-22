package com.isanechek.myapplication.data.requests

import com.isanechek.myapplication.data.models.VKUser
import com.isanechek.myapplication.data.parsers.ResponseUserParser
import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.internal.ApiCommand

class VKUserInfoRequest(
    private val uids: IntArray = intArrayOf()
) : ApiCommand<List<VKUser>>() {

    override fun onExecute(manager: VKApiManager): List<VKUser> = when {
        uids.isEmpty() -> {
            val call = VKMethodCall.Builder()
                .method("users.get")
                .args("fields", "photo_200")
                .version(manager.config.version)
                .build()
            manager.execute(call, ResponseUserParser())
        }
        else -> {
            val result = mutableListOf<VKUser>()
            val chunks = uids.toList().chunked(900)
            for (chunk in chunks) {
                val call = VKMethodCall.Builder()
                    .method("users.get")
                    .args("user_ids", chunk.joinToString(","))
                    .args("fields", "photo_200")
                    .version(manager.config.version)
                    .build()
                result.addAll(manager.execute(call, ResponseUserParser()))
            }
            result
        }
    }
}