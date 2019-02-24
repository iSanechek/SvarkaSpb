package com.isanechek.myapplication.screens.base

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.models.LoadStatus
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.setVisible
import kotlinx.android.synthetic.main.base_list_screen_layout.*
import kotlinx.android.synthetic.main.toolbar_x_layout.*

open class BaseListScreen : BaseScreen() {

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val show = recyclerView.canScrollVertically(-1)
            toolbar_x.setElevationVisibility(show)
        }
    }

    fun setupCloseButton(@DrawableRes resourceId: Int, callback: () -> Unit) {
        with(toolbar_x_back_btn) {
            setImageDrawable(ContextCompat.getDrawable(requireContext(), resourceId))
            onClick {
                callback.invoke()
            }
        }
    }

    val progressObserver = Observer<LoadStatus> { status ->
        status?.let {
            when (it) {
                is LoadStatus.Loading -> toolbar_x_progress.setVisible(true)
                is LoadStatus.Done -> toolbar_x_progress.setVisible(false)
                is LoadStatus.Error -> toolbar_x_progress.setVisible(false)
            }
        }
    }

    override fun layoutId(): Int = _layout.base_list_screen_layout

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

    open fun getToolbar() = toolbar_x

    open fun getRecyclerView() = base_list
}