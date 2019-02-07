package com.isanechek.myapplication.screens.shop

import android.content.Intent
import android.os.Bundle
import com.isanechek.myapplication.data.models.AuthState
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.isanechek.myapplication.utils.loging.DebugContract
import com.vk.api.sdk.VK
import org.koin.android.ext.android.inject

class ShowScreen : BaseListScreen() {

    private val debug: DebugContract by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbarTitle("Магазин")

        checkLogin(childFragmentManager) { state ->
            when (state) {
                is AuthState.LoadData -> Unit
                is AuthState.CloseScreen -> closeScreen()
                is AuthState.NeedLogin -> startLogin(requireActivity())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data == null || VK.onActivityResult(requestCode, resultCode, data, loginVkCallback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}