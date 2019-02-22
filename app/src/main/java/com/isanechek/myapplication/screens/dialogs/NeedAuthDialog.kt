package com.isanechek.myapplication.screens.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.isanechek.myapplication.data.models.AuthState

@SuppressLint("ValidFragment")
class NeedAuthDialog constructor(private val callback: (AuthState) -> Unit) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireActivity())
            .setMessage("Вы не авторезированы! Для доступа к магазину необходима авторизация через Вконтакте")
            .setPositiveButton("Аторизовать") { dialog, _ ->
                callback(AuthState.NeedLogin)
                dialog.dismiss()

            }.setNegativeButton("Нет, спасибо.") { dialog, _ ->
                callback(AuthState.CloseScreen)
                dialog.dismiss()
            }.create()
}