package com.isanechek.myapplication.screens.empty

import android.os.Bundle
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.screens.base.BaseScreen

class EmptyScreen : BaseScreen() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().finish()
    }

    override fun layoutId(): Int = _layout.empty_screen_layout
}