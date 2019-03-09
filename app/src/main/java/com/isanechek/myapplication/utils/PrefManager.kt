package com.isanechek.myapplication.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.isanechek.myapplication.empty

interface PrefManager {
    var loginWarningState: Boolean
    var token: String
    var isAdmin: Int
    var lastUpdateTime: Long
    var showLicense: Boolean
}

class PrefManagerImpl(
    private val preferences: SharedPreferences
) : PrefManager {
    override var showLicense: Boolean
        get() = preferences.getBoolean("show_license", true)
        set(value) {
            preferences.edit {
                putBoolean("show_license", value)
            }
        }

    override var lastUpdateTime: Long
        get() = preferences.getLong("dasboard.last.update.time", 0)
        set(value) {
            preferences.edit {
                putLong("dasboard.last.update.time", value)
            }
        }

    override var isAdmin: Int
        get() = preferences.getInt("user.is.admin", 0)
        set(value) {
            preferences.edit {
                putInt("user.is.admin", value)
            }
        }

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