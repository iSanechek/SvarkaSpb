package com.isanechek.myapplication.screens.viewer

import android.os.Bundle
import android.widget.Toast
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.empty
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.base.BaseScreen
import com.isanechek.myapplication.screens.gallery.PhotoDetailGlideDrawableImageViewTarget
import com.isanechek.myapplication.utils.GlideApp
import com.isanechek.myapplication.utils.glide.CustomProgressListener
import com.isanechek.myapplication.utils.glide.ProgressInterceptor
import kotlinx.android.synthetic.main.viewer_screen_layout.*

class ViewerScreen : BaseScreen() {

    private val urlSmall: String
        get() = arguments?.getString(ARGS) ?: String.empty()

    private val urlFull: String
        get() = arguments?.getString(ARGS_FULL) ?: String.empty()


    override fun layoutId(): Int = _layout.viewer_screen_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val url = if (urlFull.isNotEmpty()) urlFull else urlSmall
        when {
            url.isNotEmpty() -> {
                ProgressInterceptor.addListener(url, CustomProgressListener(viewer_screen_progress))
                GlideApp.with(this)
                    .load(url)
                    .into(PhotoDetailGlideDrawableImageViewTarget(viewer_image, viewer_screen_progress, url) {
                        Toast.makeText(requireActivity(), "Не удалось загрузить изображение", Toast.LENGTH_SHORT).show()
                    })
            }
            else -> Toast.makeText(requireActivity(), "Ой. Что-то пошло не так.", Toast.LENGTH_SHORT).show()
        }

        viewer_close_btn.onClick {
            closeScreen()
        }
    }

    companion object {
        const val ARGS = "viewer.args"
        const val ARGS_FULL = "viewer.args.full"
    }

}