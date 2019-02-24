package com.isanechek.myapplication.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.isanechek.myapplication.empty

interface PrefManager {
    var loginWarningState: Boolean
    var token: String
}

class PrefManagerImpl(
    private val preferences: SharedPreferences
) : PrefManager {
    override var token: String
        get() = preferences.getString("user.vk.token", String.empty()) ?: String.empty()
        set(value) {
            preferences.edit {
                putString("user.vk.token", value)
            }
        }

    override var loginWarningState: Boolean
        get() = preferences.getBoolean("login_warning_show", true)
        set(value) {
            preferences.edit {
                putBoolean("login_warning_show", value)
            }
        }

}