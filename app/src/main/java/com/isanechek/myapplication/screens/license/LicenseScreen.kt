package com.isanechek.myapplication.screens.license

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.base.BaseScreen
import com.isanechek.myapplication.utils.PrefManager
import kotlinx.android.synthetic.main.license_screen_layout.*
import org.json.JSONObject
import org.koin.android.ext.android.inject

class LicenseScreen : BaseScreen() {

    private val pref: PrefManager by inject()

    override fun layoutId(): Int = _layout.license_screen_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        readLicense()
        license_short_action.onClick {
            MaterialDialog(requireActivity()).show {
                title(text = "В двух словах")
                message(text = "Разработчики в лице AverdSoft не несёт ответственности за весь контент представленный в данном приложение или услуги.")
                cancelOnTouchOutside(false)
                positiveButton(text = "Я согласен") { dialog ->
                    pref.showLicense = false
                    dialog.dismiss()
                    closeScreen()
                }
                negativeButton(text = "До свидания") { dialog ->
                    dialog.dismiss()
                    showNegativeDialog()
                }
            }
        }

        license_is_ok.onClick {
            pref.showLicense = false
            closeScreen()
        }

        license_close_app.onClick {
            showNegativeDialog()
        }
    }

    private fun showNegativeDialog() {
        MaterialDialog(requireActivity()).show {
            title(text = "Предупреждение!")
            cancelOnTouchOutside(false)
            message(text = "Вы не согласились с представленным выше лицензионным соглашением. Дальнейшая работа с приложение невозможна. Если Вы передумали - просто перезапустите приложение.")
            positiveButton(text = "Закрыть") {
                it.dismiss()
                requireActivity().finish()
            }
        }
    }

    private fun readLicense() {
        val stringJson = requireContext()
            .applicationContext
            .assets
            .open("worker.json")
            .bufferedReader()
            .use {
                it.readText()
            }
        val jo = JSONObject(stringJson)
        val licenseText = jo.getString("license")
        license_info.text = licenseText
    }
}