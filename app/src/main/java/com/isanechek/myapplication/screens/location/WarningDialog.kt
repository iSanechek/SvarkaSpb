package com.isanechek.myapplication.screens.location

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class WarningDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setTitle("Внимание")
            .setMessage("Звонок будет совершён на номер зарегистрированный в Санкт-Петербурге.")
            .setPositiveButton("Позвонить") { dialog, _ ->
                val i = Intent(Intent.ACTION_DIAL)
                i.data = Uri.parse("tel:+79312070664")
                startActivity(i)
                dialog?.dismiss()
            }.setNegativeButton("Отменить") { dialog, _ ->
                dialog?.dismiss()
            }.setCancelable(true)
            .create()
    }
}