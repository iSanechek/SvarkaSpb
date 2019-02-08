package com.isanechek.myapplication.screens.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.models.AuthState
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.base.BaseScreen
import com.isanechek.myapplication.screens.dialogs.NeedAuthDialog
import com.isanechek.myapplication.utils.PrefManager
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.auth_screen_layout.*
import org.koin.android.ext.android.inject

class AuthScreen : BaseScreen() {

    private val pref: PrefManager by inject()

    override fun layoutId(): Int = _layout.auth_screen_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        login_start_action.onClick {
            when {
                pref.loginWarningState -> {
                    showWarningDialog()
                    pref.loginWarningState = false
                }
                else -> startAuth()
            }
        }

        login_info_action.onClick {
            showWarningDialog()
            pref.loginWarningState = false
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val callback = object : VKAuthCallback {

            override fun onLogin(token: VKAccessToken) {
                Log.d("TEST", "Token ${token.userId}")
                findNavController().navigateUp()

            }

            override fun onLoginFailed(errorCode: Int) {
                Toast.makeText(requireActivity(), "При авторизации произошла ошибка!", Toast.LENGTH_SHORT).show()
            }
        }
        if (data == null || VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showWarningDialog() {
        NeedAuthDialog { state ->
            when (state) {
                is AuthState.CloseScreen -> findNavController().popBackStack()
                is AuthState.NeedLogin -> startAuth()
            }

        }.show(childFragmentManager, "auth_dialog")
    }

    private fun startAuth() {
        VK.login(
            requireActivity(), arrayListOf(
                VKScope.WALL,
                VKScope.GROUPS,
                VKScope.MARKET,
                VKScope.MESSAGES,
                VKScope.PHOTOS,
                VKScope.VIDEO
            )
        )
    }
}