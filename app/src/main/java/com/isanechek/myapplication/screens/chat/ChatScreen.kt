package com.isanechek.myapplication.screens.chat

import android.content.Intent
import android.os.Bundle
import com.isanechek.myapplication.screens.auth.AuthScreen
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.vk.api.sdk.VK

class ChatScreen : BaseListScreen() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!VK.isLoggedIn()) {
            startActivity(Intent(requireActivity(), AuthScreen::class.java))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }
}