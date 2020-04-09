package com.isanechek.myapplication.screens.gallery

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.request.RequestOptions
import com.isanechek.myapplication.*
import com.isanechek.myapplication.data.Photo
import com.isanechek.myapplication.data.models.LoadStatus
import com.isanechek.myapplication.screens.base.BaseListScreen
import com.isanechek.myapplication.screens.base.bind
import com.isanechek.myapplication.screens.viewer.ViewerScreen
import com.isanechek.myapplication.utils.GlideApp
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.photo_item_layout.view.*
import kotlinx.android.synthetic.main.toolbar_x_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotosScreen : BaseListScreen() {

    private val vm: PhotosViewModel by viewModel()

    private val diffCallback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem == newItem
    }

    override fun bindUi(view: View, savedInstanceState: Bundle?) {
        setToolbarTitle("Галерея")
        setupCloseButton(_drawable.ic_close_black_24dp) {
            closeScreen()
        }

        bindAds(if (BuildConfig.DEBUG) "R-M-DEMO-320x50" else "R-M-354145-2")
        vm.progress.observe(this, Observer { status ->
            status?.let {
                when (it) {
                    is LoadStatus.Loading -> toolbar_x_progress.setVisible(true)
                    is LoadStatus.Done -> toolbar_x_progress.setVisible(false)
                    is LoadStatus.Fail -> {
                        if (toolbar_x_progress != null) {
                            if (toolbar_x_progress.isVisible) toolbar_x_progress.isInvisible = true
                        }
                        when (it.error) {
                            is LoadStatus.Error.NetworkError -> {
                                Toast.makeText(
                                    requireContext(),
                                    _text.no_network_error_msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                            }
                        }

                    }
                }
            }
        })
        getRecyclerView().bind(diffCallback, _layout.photo_item_layout) { photo: Photo ->
            GlideApp.with(photo_item_cover.context)
                .load(photo.smallUrl)
                .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(8, 0)))
                .into(photo_item_cover)

            photo_item_container.onClick {
                findNavController().navigate(
                    _id.go_from_photos_to_viewer, bundleOf(
                        ViewerScreen.ARGS to photo.smallUrl,
                        ViewerScreen.ARGS_FULL to photo.fullUrl
                    )
                )
            }
        }.submitList(vm.data)
    }

}