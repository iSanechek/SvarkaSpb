package com.isanechek.myapplication.screens.location

import android.os.Bundle
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.isanechek.myapplication.screens.base.BaseScreen

class LocationScreen : BaseListScreen() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    companion object {
        private const val EMAIL = 0
        private const val VK = 1
        private const val INFO = 2
        private const val PHONE = 3
        private const val TAG = "About"
    }

}