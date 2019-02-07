package com.isanechek.myapplication.screens.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class NeedAuthDialog : DialogFragment() {

    interface Callback {
        fun positive()
        fun negative()
    }

    private var callback: Callback? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireActivity())
            .setMessage("Вы не авторезированы! Для доступа к магазину необходима авторизация через Вконтакте")
            .setPositiveButton("Аторизовать") { dialog, _ ->
                callback?.positive()
                dialog.dismiss()

            }.setNegativeButton("Нет, спасибо.") { dialog, _ ->
                callback?.negative()
                dialog.dismiss()
            }.create()


    fun setCallback(callback: Callback) {
        this.callback = callback
    }
}