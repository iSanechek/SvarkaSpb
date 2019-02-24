package com.isanechek.myapplication.screens.shop

import com.isanechek.myapplication.screens.base.BaseViewModel
import com.isanechek.myapplication.utils.PrefManager
import com.isanechek.myapplication.utils.loging.DebugContract
import kotlinx.coroutines.launch

class ShopViewModel(private val pref: PrefManager,
                    private val debug: DebugContract) : BaseViewModel() {

    fun loadData() {
        debug.log("loadData() start")
        val token = pref.token
        debug.log("token $token")
        addJob(job = launch(context = coroutineContext) {

        }, key = "market.all.items")
    }

    fun updateDate() {

    }

}