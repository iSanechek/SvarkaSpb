package com.isanechek.myapplication.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.screens.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DebugActivity : AppCompatActivity() {

    private val vm: DashboardViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_layout.dashboard_screen_layout2)

        vm.loadPhotos()

    }
}