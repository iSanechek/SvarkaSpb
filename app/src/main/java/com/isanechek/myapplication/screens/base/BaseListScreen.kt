package com.isanechek.myapplication.screens.base

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.setVisible
import com.isanechek.myapplication.widgets.ToolbarX
import com.yandex.mobile.ads.AdRequest
import com.yandex.mobile.ads.AdSize
import kotlinx.android.synthetic.main.base_list_screen_layout.*
import kotlinx.android.synthetic.main.toolbar_x_layout.*

abstract class BaseListScreen : BaseScreen() {

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val show = recyclerView.canScrollVertically(-1)
            toolbar_x.setElevationVisibility(show)
        }
    }

    abstract fun bindUi(view: View, savedInstanceState: Bundle?)

    override fun layoutId(): Int = _layout.base_list_screen_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUi(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        base_list.addOnScrollListener(scrollListener)
    }

    override fun onPause() {
        base_list.removeOnScrollListener(scrollListener)
        super.onPause()
    }

    open fun setToolbarTitle(@StringRes titleId: Int) {
        setToolbarTitle(getString(titleId))
    }

    open fun setToolbarTitle(title: String) {
        toolbar_x_title.text = title
    }

    open fun showToolbarProgress(hide: Boolean) {
        toolbar_x_progress.setVisible(hide)
    }

    open fun getToolbar(): ToolbarX = toolbar_x

    open fun getRecyclerView(): RecyclerView = this.base_list

    open fun bindAds(adsKey: String) {
        with(list_ads) {
            blockId = adsKey
            adSize = AdSize.BANNER_320x50
            loadAd(AdRequest.builder().build())
        }
    }
}