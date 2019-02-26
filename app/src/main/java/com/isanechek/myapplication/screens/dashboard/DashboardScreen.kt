package com.isanechek.myapplication.screens.dashboard

import android.os.Bundle
import android.view.View
import com.bannerlayout.OnBannerClickListener
import com.isanechek.myapplication._drawable
import com.isanechek.myapplication._id
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.models.Banner
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.base.BaseScreen
import com.isanechek.myapplication.utils.ImageManager
import com.isanechek.myapplication.utils.SimpleData
import kotlinx.android.synthetic.main.dashboard_screen_layout.*

class DashboardScreen : BaseScreen() {

    override fun layoutId(): Int = _layout.dashboard_screen_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        handleSetupUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        banner_view.removeHandler()
    }

    private val bannerClickListener = object : OnBannerClickListener<Banner> {
        override fun onBannerClick(view: View, position: Int, model: Banner) {

        }
    }

    private fun handleSetupUi() {
        banner_view.apply {
            delayTime = 2000
            dotsSelector = _drawable.banner
            pageNumViewBottomMargin = 16
            pageNumViewLeftMargin = 16
            pageNumViewTopMargin = 16
            pageNumViewRightMargin = 16
            imageLoaderManager = ImageManager()
            onBannerClickListener = bannerClickListener
        }
            .initPageNumView()
            .initTips()
            .resource(SimpleData.initModel())

        // implementation click listener

        sq_bottom_right.onClick {
            goToScree(_id.go_from_dashboard_to_info)
        }

        sq_bottom_left.onClick {
            goToScree(_id.go_from_dashboard_to_market)
        }

        imageButton.onClick {
            goToScree(_id.go_from_dashboard_to_about)
        }
    }

}