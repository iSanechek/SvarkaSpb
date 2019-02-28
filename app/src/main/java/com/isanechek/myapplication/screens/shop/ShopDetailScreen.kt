package com.isanechek.myapplication.screens.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.isanechek.myapplication.*
import com.isanechek.myapplication.data.models.Three
import com.isanechek.myapplication.data.requests.VkMarketItemDetailRequest
import com.isanechek.myapplication.screens.base.BaseScreen
import com.isanechek.myapplication.screens.viewer.ViewerScreen
import com.isanechek.myapplication.utils.GlideApp
import com.isanechek.myapplication.utils.PrefManager
import com.isanechek.myapplication.utils.loging.DebugContract
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.market_detail_screen_layout2.*
import kotlinx.android.synthetic.main.market_gallery_item_layout.*
import kotlinx.android.synthetic.main.toolbar_x_layout.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.koin.android.ext.android.inject

class ShopDetailScreen : BaseScreen() {

    private val debug: DebugContract by inject()
    private val pref: PrefManager by inject()
    private val itemId: String
        get() = arguments?.getString(ARGS_MARKET_DETAIL_ID) ?: String.empty()

    override fun layoutId(): Int = _layout.market_detail_screen_layout2

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        debug.log("Detail Shop Screen id $itemId")
        if (itemId.isEmpty()) {
            showErrorView()
            return
        }
        setupCloseButton(_drawable.ic_arrow_back_black_24dp) {
            closeScreen()
        }
        loadContent()
    }

    private fun loadContent() {
        shop_detail_content_progress_container.setVisible(true)
        val isAdmin = pref.isAdmin
        debug.log("Detail Shop Screen Load Content! Admin? -> $isAdmin")
        VK.execute(VkMarketItemDetailRequest(itemId, 1), object: VKApiCallback<JSONObject> {
            override fun fail(error: VKApiExecutionException) {
                debug.sendStackTrace("Load Market detail error", error)
                showErrorView()
            }

            override fun success(result: JSONObject) {

                shop_detail_content_progress_container.setVisible(false)
                shop_detail_content_container.setVisible(true)
                try {
                    val response = result.getJSONObject("response")
//                    val count = response.getInt("count")
                    debug.log(result.toString())
                    val item = response.getJSONArray("items").getJSONObject(0)
                    val id = item.getInt("id")
                    val ownerId = item.getInt("owner_id")
                    val title = item.getString("title")
//                    shop_detail_content_title.text = title
                    toolbar_x_title.text = title
                    val description = item.getString("description")
                    shop_detail_content_description.text = description
                    val price = item.getJSONObject("price").getString("text")
                    shop_detail_content_price.text = "цена: $price"
                    val date = item.getLong("date")
                    val cover = item.getString("thumb_photo")
                    loadCover(cover)
//                    val favorite = item.getBoolean("is_favorite")
                    setupFab(false)
                    val photos = parsePhotos(item.getJSONArray("photos"), PREVIEW_PHOTO_TYPE)
                    debug.log("photos size ${photos.size}")
                    setupGallery(photos)
                    if (isAdmin == 1) {
                        val likes = item.getJSONObject("likes")
                        val count = likes.getInt("count")
                        val viewsCount = item.getInt("views_count")
                    }

                    shop_detail_content_message_btn.onClick {
                        requireActivity().actionView { "https://vk.com/svarochnye_raboty_tosno" }
                    }

                } catch (ex: JSONException) {
                    debug.sendStackTrace("Market Detail Parser Error!", ex)
                    showErrorView()
                }
            }
        })
    }

    private fun setupGallery(data: List<Three>) {
        with(shop_detail_content_gallery) {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            adapter = GalleryAdapter(data) { item, position ->
                findNavController().navigate(_id.go_from_shop_detail_to_viewer, bundleOf(ViewerScreen.ARGS to item.url))
            }
            setHasFixedSize(true)
        }
    }

    internal class GalleryAdapter(
        private val items: List<Three>,
        private val callback: (Three, Int) -> Unit
    ) : RecyclerView.Adapter<GalleryAdapter.GalleryHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder =
            GalleryHolder(LayoutInflater.from(parent.context)
                .inflate(
                    _layout.market_gallery_item_layout,
                    parent, false
                )
            )

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
            holder.bindTo(items[position], callback, position)
        }

        class GalleryHolder(
            override val containerView: View
        ) : RecyclerView.ViewHolder(containerView), LayoutContainer {
            fun bindTo(item: Three, callback: (Three, Int) -> Unit, position: Int) {
                GlideApp.with(market_gallery_item_cover.context).load(item.url).into(market_gallery_item_cover)
                market_gallery_item_container.onClick {
                    callback(item, position)
                }
            }
        }
    }

    private fun setupFab(bookmark: Boolean) {
//        with(shop_detail_fab) {
//            setImageDrawable(if (bookmark) getIcon(_drawable.ic_bookmark_black_24dp) else getIcon(_drawable.ic_bookmark_border_white_24dp))
//            onClick {
//                if (bookmark) {
//                    // implementation remove item from bookmark
//                } else {
//                    // implementation add item to bookmark
//                }
//            }
//        }
    }

    private fun getIcon(@DrawableRes iconId: Int) = ContextCompat.getDrawable(requireContext(), iconId)

    private fun loadCover(url: String) {
        GlideApp.with(this)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(8, 0)))
            .into(shop_detail_cover)
    }

    private fun parsePhotos(ja: JSONArray, requestType: String): List<Three> {
        val temp = mutableListOf<Three>()
        for (i in 0 until ja.length()) {
            val photo = ja.getJSONObject(i)
            val sizes = photo.getJSONArray("sizes")
            for (s in 0 until sizes.length()) {
                val size = sizes.getJSONObject(s)
                val type = size.getString("type")
                if (requestType == type) {
                    temp.add(Three(
                        url = size.getString("url"),
                        text = photo.getString("text"),
                        date = photo.getLong("date")
                    ))
                }
            }
        }
        return temp
    }

    private fun showErrorView() {
        when {
            shop_detail_content_progress_container.visibility == View.VISIBLE -> shop_detail_content_progress_container.setVisible(false)
            shop_detail_content_container.visibility == View.VISIBLE -> shop_detail_content_container.setVisible(false)
        }
        shop_detail_content_error_container.setVisible(true)
    }

    companion object {
        const val ARGS_MARKET_DETAIL_ID = "market.detail.id"
        private const val PREVIEW_PHOTO_TYPE = "p"
        private const val FULL_PHOTO_TYPE = "w"
    }

}