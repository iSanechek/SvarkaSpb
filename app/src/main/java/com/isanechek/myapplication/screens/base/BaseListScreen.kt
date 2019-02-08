package com.isanechek.myapplication.screens.base

import android.app.Activity
import android.util.Log
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.models.AuthState
import com.isanechek.myapplication.data.models.LoadStatus
import com.isanechek.myapplication.screens.dialogs.NeedAuthDialog
import com.isanechek.myapplication.setVisible
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.base_list_screen_layout.*
import kotlinx.android.synthetic.main.toolbar_x_layout.*

open class BaseListScreen : BaseScreen() {

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val show = recyclerView.canScrollVertically(-1)
            toolbar_x.setElevationVisibility(show)
        }
    }

    val loginVkCallback = object : VKAuthCallback {

        override fun onLogin(token: VKAccessToken) {
            Log.d("TEST", "Token ${token.userId}")
        }

        override fun onLoginFailed(errorCode: Int) {
            Log.d("TEST", "Error code $errorCode")
        }
    }

    val progressObserver = Observer<LoadStatus> { status ->
        status?.let {
            when (it) {
                is LoadStatus.Loading -> toolbar_x_progress.setVisible(true)
                is LoadStatus.Done -> toolbar_x_progress.setVisible(false)
                is LoadStatus.Error -> toolbar_x_progress.setVisible(false)
            }
        }
    }

    override fun layoutId(): Int = _layout.base_list_screen_layout

    override fun onResume() {
        super.onResume()
        base_list.addOnScrollListener(scrollListener)
    }

    override fun onPause() {
        base_list.removeOnScrollListener(scrollListener)
        super.onPause()
    }

    open fun setToolbarTitle(@StringRes titleId: Int) {
        setToolbarTitle(getString(titleId))
    }

    open fun setToolbarTitle(title: String) {
        toolbar_x_title.text = title
    }

    open fun getToolbar() = toolbar_x

    open fun getRecyclerView() = base_list

    fun checkLogin(manager: FragmentManager, callback: (AuthState) -> Unit) {
        when {
            VK.isLoggedIn() -> callback(AuthState.LoadData)
            else -> NeedAuthDialog {
                callback(it)
            }.show(manager, "auth_dialog")
        }
    }

    fun startLogin(context: Activity) {

    }
}