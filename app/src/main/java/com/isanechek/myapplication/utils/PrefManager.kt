package com.isanechek.myapplication.utils

import android.content.SharedPreferences
import androidx.core.content.edit

interface PrefManager {
    var loginWarningState: Boolean
}

class PrefManagerImpl(
    private val preferences: SharedPreferences
) : PrefManager {

    override var loginWarningState: Boolean
        get() = preferences.getBoolean("login_warning_show", true)
        set(value) {
            preferences.edit {
                putBoolean("login_warning_show", value)
            }
        }

}