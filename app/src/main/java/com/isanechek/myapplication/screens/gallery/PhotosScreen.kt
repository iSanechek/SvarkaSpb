package com.isanechek.myapplication.screens.gallery

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.request.RequestOptions
import com.isanechek.myapplication._drawable
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.Photo
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.auth.AuthScreen
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.isanechek.myapplication.screens.base.bind
import com.isanechek.myapplication.utils.GlideApp
import com.vk.api.sdk.VK
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.photo_item_layout.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotosScreen : BaseListScreen() {

    private val vm: PhotosViewModel by viewModel()

    private val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!VK.isLoggedIn()) {
            requireActivity().startActivity(Intent(requireActivity(), AuthScreen::class.java))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setToolbarTitle("Галерея")
        setupCloseButton(_drawable.ic_close_black_24dp) {
            closeScreen()
        }

        vm.progress.observe(this, progressObserver)
        getRecyclerView().bind(diffCallback, _layout.photo_item_layout) { photo: Photo ->
            GlideApp.with(photo_item_cover.context)
                .load(photo.smallUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(8, 0)))
                .into(photo_item_cover)

            photo_item_container.onClick {

            }
        }.submitList(vm.data)
    }

}