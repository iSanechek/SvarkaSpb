package com.isanechek.myapplication.screens.location

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.isanechek.myapplication.BuildConfig

class InfoAppDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle("Информация о приложение")
            .setMessage("Версия приложения ${BuildConfig.VERSION_NAME}")
            .setPositiveButton("Ok") { dialog, _ ->
                dialog?.dismiss()
            }.setCancelable(true)
            .create()
    }
}