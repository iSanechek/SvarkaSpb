package com.isanechek.myapplication.screens.info

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.isanechek.myapplication._drawable
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.models.Info
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.isanechek.myapplication.screens.base.bind
import kotlinx.android.synthetic.main.info_item_layout.view.*

class InfoScreen : BaseListScreen() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbarTitle("Виды работ")
        setupCloseButton(_drawable.ic_close_black_24dp) {
            findNavController().navigateUp()
        }
        getRecyclerView()
            .bind(InfoData.inits(), _layout.info_item_layout) { info: Info ->
                info_item_title.text = info.title
                info_item_description.text = info.message
                info_item_action.onClick {

                }
            }
    }
}