package com.isanechek.myapplication.screens.location

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.isanechek.myapplication.*
import com.isanechek.myapplication.data.models.AboutItem
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.isanechek.myapplication.screens.base.bind
import com.isanechek.myapplication.utils.GlideApp
import kotlinx.android.synthetic.main.about_item_layout.view.*
import kotlinx.android.synthetic.main.info_item_header_layout.view.*

class LocationScreen : BaseListScreen() {

    private val data = listOf(
        AboutItem(
            id = HEADER,
            title = "",
            icon = 0,
            data = ""
        ),
        AboutItem(
            id = EMAIL,
            title = "Написать на e-mail",
            icon = 0,
            data = "ermakoff2009@rambler.ru"
        ),
        AboutItem(
            id = VK,
            title = "Посетить группу в ВК",
            icon = 0,
            data = "https://vk.com/svarochnye_raboty_tosno"
        ),
        AboutItem(
            id = PHONE,
            title = "Позвонить",
            icon = 0,
            data = "+79312070664"
        ),
        AboutItem(
            id = INFO,
            title = "Информация о приложении",
            icon = 0,
            data = ""
        ),
        AboutItem(
            id = DEV_WEB,
            title = "Сайт разработчиков",
            icon = 0,
            data = "https://averd-soft.ru"
        )
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setToolbarTitle("Контактактная информация")
        setupCloseButton(_drawable.ic_close_black_24dp) {
            closeScreen()
        }

        getRecyclerView().bind(data)
            .map(_layout.info_item_header_layout, predicate = { it.id == 4 }) {
                GlideApp.with(info_header_iv.context).load(_drawable.ic_launcher).into(info_header_iv)
            }
            .map(_layout.about_item_layout, predicate = { it.id != 4 }) { item: AboutItem ->
                about_item_title.text = item.title
                with(about_item_container) {
                    onClick { clickAction(item) }
                    setOnLongClickListener {
                        if (item.id != INFO) {
                            copyDataAction(item.data)
                        }
                        true
                    }
                }
            }
    }

    private fun clickAction(item: AboutItem) {
        when(item.id) {
            EMAIL -> {
                requireActivity().sendEmail("Сварска Спб", item.data, "Написать нам")
            }
            VK, DEV_WEB -> requireActivity().actionView { item.data }
            INFO -> InfoAppDialog().show(childFragmentManager, "AppVersionDialog")
            PHONE -> WarningDialog().show(childFragmentManager, "WarningDialog")
        }
    }

    private fun copyDataAction(data: String) {
        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Info_data", data)
        clipboard.primaryClip = clip

        Toast.makeText(requireActivity(), "Данные скопированы в буфер обмена", Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val EMAIL = 0
        private const val VK = 1
        private const val INFO = 2
        private const val PHONE = 3
        private const val HEADER = 4
        private const val DEV_WEB = 5
        private const val TAG = "About"
    }

}