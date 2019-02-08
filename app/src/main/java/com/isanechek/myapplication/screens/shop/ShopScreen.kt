package com.isanechek.myapplication.screens.shop

import android.os.Bundle
import android.widget.Toast
import com.isanechek.myapplication._id
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.models.market.Market
import com.isanechek.myapplication.data.models.market.MarketItem
import com.isanechek.myapplication.data.requests.VkMarketAllItemsRequest
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.isanechek.myapplication.screens.base.bind
import com.isanechek.myapplication.utils.loging.DebugContract
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.android.synthetic.main.market_item_layout.view.*
import org.koin.android.ext.android.inject

class ShowScreen : BaseListScreen() {

    private val debug: DebugContract by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!VK.isLoggedIn()) {
            goToScree(_id.go_from_shop_to_auth)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setToolbarTitle("Магазин")

    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        debug.log("BOOM")
        if (!VK.isLoggedIn()) return
        debug.log("BOOM 2")
        VK.execute(VkMarketAllItemsRequest(-125640924), object : VKApiCallback<Market> {
            override fun fail(error: VKApiExecutionException) {
                Toast.makeText(requireActivity(), "Error ${error.code}", Toast.LENGTH_SHORT).show()
            }

            override fun success(result: Market) {
                val count = result.itemsSize
                debug.log("Market All Items Size $count")
                if (count > 0) {
                    getRecyclerView()
                        .bind(result.items, _layout.market_item_layout) { item: MarketItem ->
                            market_item_title.text = item.title
                        }
                }
            }
        })
    }
}