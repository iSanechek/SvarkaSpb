package com.isanechek.myapplication.screens.info

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.isanechek.myapplication.*
import com.isanechek.myapplication.data.models.AboutItem
import com.isanechek.myapplication.data.models.Info
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.isanechek.myapplication.screens.base.bind
import kotlinx.android.synthetic.main.info_item_layout.view.*

class InfoScreen : BaseListScreen() {

    private val list = listOf(
        AboutItem(
            id = VK_ID,
            icon = 0,
            data = "https://vk.com/svarochnye_raboty_tosno",
            title = "Посетить группу в вк"
        ),
        AboutItem(
            id = EMAIL_ID,
            icon = 0,
            data = "ermakoff2009@rambler.ru",
            title = "Написать e-mail"
        ),
        AboutItem(
            id = CALL_ID,
            icon = 0,
            data = "+79312070664",
            title = "Позвонить"
        ),
        AboutItem(
            id = VIBER_ID,
            icon = 0,
            data = "+79312070664",
            title = "Написать в viber"
        ),
        AboutItem(
            id = COPY_VK_ID,
            icon = 0,
            data = "https://vk.com/svarochnye_raboty_tosno",
            title = "Скопировать url вк"
        ),
        AboutItem(
            id = COPY_PHONE_ID,
            icon = 0,
            data = "+79312070664",
            title = "Скопировать номер телефона"
        )
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbarTitle("Виды работ")
        setupCloseButton(_drawable.ic_close_black_24dp) {
            findNavController().navigateUp()
        }

        bindAds("R-M-DEMO-320x50")
        getRecyclerView()
            .bind(InfoData.inits(), _layout.info_item_layout) { info: Info ->
                info_item_title.text = info.title
                info_item_description.text = info.message
                info_item_action.onClick {
                    MaterialDialog(requireActivity()).show {
                        title(text = "Выбрать способ")
                        listItems(items = list.map { it.title }.toList()) { dialog, index, _ ->
                            val item = list[index]
                            when (item.id) {
                                VK_ID -> requireActivity().actionView { item.data }
                                EMAIL_ID -> requireActivity().sendEmail("Сварска Спб", item.data, "Написать нам")
                                VIBER_ID -> {
                                    val phoneNumber = "tel:${item.data}"
                                    Intent(Intent.ACTION_VIEW)
                                        .run {
                                            setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity")
                                            data = phoneNumber.toUri()
                                            requireActivity().startActivity(this)
                                        }
                                }
                                CALL_ID -> {
                                    val i = Intent(Intent.ACTION_DIAL)
                                    i.data = Uri.parse("tel:+79312070664")
                                    requireActivity().startActivity(i)
                                }
                                COPY_PHONE_ID -> copyDataAction(item.data)
                                COPY_VK_ID -> copyDataAction(item.data)
                            }
                            dialog.dismiss()
                        }
                    }
                }
            }
    }

    private fun copyDataAction(data: String) {
        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Info_data", data)
        clipboard.primaryClip = clip
        Toast.makeText(requireActivity(), "Данные скопированы в буфер обмена", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val VK_ID = 0
        private const val EMAIL_ID = 1
        private const val VIBER_ID = 2
        private const val CALL_ID = 3
        private const val COPY_PHONE_ID = 4
        private const val COPY_VK_ID = 5
    }
}