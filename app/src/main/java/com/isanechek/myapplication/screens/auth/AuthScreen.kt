package com.isanechek.myapplication.screens.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.models.AuthState
import com.isanechek.myapplication.data.models.VKUser
import com.isanechek.myapplication.data.requests.VKUserInfoRequest
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.dialogs.NeedAuthDialog
import com.isanechek.myapplication.setVisible
import com.isanechek.myapplication.utils.GlideApp
import com.isanechek.myapplication.utils.PrefManager
import com.isanechek.myapplication.utils.glide.CustomGlideDrawableImageViewTarget
import com.isanechek.myapplication.utils.glide.CustomProgressListener
import com.isanechek.myapplication.utils.glide.ProgressInterceptor
import com.isanechek.myapplication.utils.loging.DebugContract
import com.isanechek.myapplication.widgets.MultiImageView
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.android.synthetic.main.auth_screen_layout.*
import org.koin.android.ext.android.inject

class AuthScreen : AppCompatActivity() {

    private val pref: PrefManager by inject()
    private val debug: DebugContract by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_layout.auth_screen_layout)

//        auth_screen_avatar.setImageDrawable(ContextCompat.getDrawable(this, _drawable.ic_mood_bad_black_96dp))

        login_start_action.onClick {
            when {
                pref.loginWarningState -> {
                    showWarningDialog()
                    debug.log("Show about dialog!")
                    pref.loginWarningState = false
                }
                else -> startAuth()
            }
        }

        login_info_action.onClick {
            showWarningDialog()
            debug.log("Start login!")
            pref.loginWarningState = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        debug.log("onActivityResult()")
        val callback = object : VKAuthCallback {

            override fun onLogin(token: VKAccessToken) {
                debug.log( "Token ${token.userId}")
                loadUserInfo()
            }

            override fun onLoginFailed(errorCode: Int) {
                debug.log("Fail error code $errorCode")
                showErrorState("При авторизации произошла ошибка",
                    "Информация об ошибке была отправлена разработчикам. Спасибо за понимание!", true)
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)

            debug.log("onActivityResult() super")
        }
    }

    private fun showWarningDialog() {
        NeedAuthDialog { state ->
            when (state) {
                is AuthState.CloseScreen -> finish()
                is AuthState.NeedLogin -> startAuth()
            }

        }.show(supportFragmentManager, "auth_dialog")
    }

    private fun startAuth() {
        VK.login(
            this, arrayListOf(
                VKScope.WALL,
                VKScope.GROUPS,
                VKScope.MARKET,
                VKScope.MESSAGES,
                VKScope.PHOTOS,
                VKScope.VIDEO
            )
        )
    }

    private fun loadUserInfo() {
        when {
            auth_screen_default_state.visibility == View.VISIBLE -> auth_screen_default_state.setVisible(false)
            auth_screen_error_state.visibility == View.VISIBLE -> auth_screen_error_state.setVisible(false)
        }
        auth_screen_done_state.setVisible(true)
        VK.execute(VKUserInfoRequest(), object : VKApiCallback<List<VKUser>> {
            override fun fail(error: VKApiExecutionException) {
                debug.sendStackTrace("loadUserInfo()", error)
                showErrorState("При загрузке данных произошла ошибка.", error.message ?: "")
            }

            override fun success(result: List<VKUser>) {
                if (result.isNotEmpty()) {
                    val user = result[0]
                    debug.log("user ${user.firstName}")
                    val avatarUrl = user.photo.replaceAfter(".jpg", "").trim()
                    debug.log("user avatar url $avatarUrl")
                    if (!TextUtils.isEmpty(avatarUrl)) {
                        auth_screen_user_avatar.shape = MultiImageView.Shape.CIRCLE
                        ProgressInterceptor.addListener(avatarUrl, CustomProgressListener(auth_screen_progress))
                        GlideApp.with(this@AuthScreen).load(user.photo).into(CustomGlideDrawableImageViewTarget(
                            view = auth_screen_user_avatar,
                            title = auth_screen_user_title,
                            description = auth_screen_username,
                            username = "${user.firstName} ${user.lastName}",
                            roundProgressBar = auth_screen_progress,
                            url = avatarUrl
                        ))
                    }
                }

//                delay(300) {
//                    finish()
//                }
            }
        })
    }

    private fun showErrorState(title: String, message: String, authError: Boolean = false) {
        when {
            authError -> auth_screen_default_state.setVisible(false)
            else -> auth_screen_done_state.setVisible(false)
        }
        auth_screen_error_state.setVisible(true)
        auth_screen_error_title.text = title
        auth_screen_error_message.text = message
        auth_screen_retry_btn.onClick {
            when {
                authError -> startAuth()
                else -> loadUserInfo()
            }
        }
        auth_screen_error_close_btn.onClick {
            finish()
        }
    }
}