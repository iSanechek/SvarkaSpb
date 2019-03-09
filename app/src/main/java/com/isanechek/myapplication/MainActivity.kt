package com.isanechek.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    private val controller: NavController by lazy {
        findNavController(_id.container_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_layout.main_layout)
        controller
    }

    override fun onSupportNavigateUp(): Boolean = controller.navigateUp()
}