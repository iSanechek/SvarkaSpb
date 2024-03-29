package com.isanechek.myapplication.screens.shop

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.request.RequestOptions
import com.isanechek.myapplication._drawable
import com.isanechek.myapplication._id
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.DbContract
import com.isanechek.myapplication.data.entity.MarketEntity
import com.isanechek.myapplication.data.models.market.Market
import com.isanechek.myapplication.data.models.market.MarketItem
import com.isanechek.myapplication.data.requests.VkMarketAllItemsRequest
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.auth.AuthScreen
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.isanechek.myapplication.screens.base.bind
import com.isanechek.myapplication.utils.GlideApp
import com.isanechek.myapplication.utils.loging.DebugContract
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.market_item_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowScreen : BaseListScreen() {

    private val debug: DebugContract by inject()
    private val db: DbContract by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!VK.isLoggedIn()) {
            requireActivity().startActivity(Intent(requireActivity(), AuthScreen::class.java))
        }
    }

    override fun bindUi(view: View, savedInstanceState: Bundle?) {
        setToolbarTitle("Магазин")
        setupCloseButton(_drawable.ic_close_black_24dp) {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()
        showData()
    }

    private fun loadData() {
        if (!VK.isLoggedIn()) return
        showToolbarProgress(true)
        VK.execute(VkMarketAllItemsRequest(), object : VKApiCallback<Market> {
            override fun fail(error: VKApiExecutionException) {
                debug.log("Error response ${error.message}")
                showToolbarProgress(false)
                Toast.makeText(requireActivity(), "Error ${error.code}", Toast.LENGTH_SHORT).show()
            }

            override fun success(result: Market) {
                showToolbarProgress(false)
                val count = result.itemsSize
                debug.log("Market All Items Size $count")
                if (count > 0) {
                    GlobalScope.launch(Dispatchers.Main) {
                        async {
                            for (item in result.items) {
                                db.insertItem(item)
                            }
                        }.await()

                        showData()
                    }
                }
            }
        })
    }

    private fun showData() {
        GlobalScope.launch(Dispatchers.Main) {
            val data = async {
                db.getItems()
            }

            val items = data.await()
            if (items.isEmpty()) {
                loadData()
                return@launch
            }
            getRecyclerView()
                .bind(items, _layout.market_item_layout) { item: MarketEntity ->
                    market_item_title.text = item.title
                    market_item_price.text = "цена ${item.price}"
                    GlideApp.with(market_item_cover.context)
                        .load(item.coverUrl)
                        .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(8, 0)))
                        .into(market_item_cover)

                    market_item_container.onClick {
                        val detailItemId = "${item.ownerId}_${item.id}"
                        debug.log("Go to detail screen with id $detailItemId")
                        findNavController().navigate(
                            _id.go_shop_from_master_to_detail,
                            bundleOf(
                                ShopDetailScreen.ARGS_MARKET_DETAIL_ID to detailItemId
                            )
                        )
                    }
                }
        }
    }

    private fun getIcon(@DrawableRes iconId: Int) = ContextCompat.getDrawable(requireContext(), iconId)
}