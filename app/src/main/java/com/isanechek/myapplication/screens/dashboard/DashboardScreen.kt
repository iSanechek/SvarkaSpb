package com.isanechek.myapplication.screens.dashboard

import android.os.Bundle
import android.view.View
import com.bannerlayout.OnBannerClickListener
import com.isanechek.myapplication._drawable
import com.isanechek.myapplication._id
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.DbContract
import com.isanechek.myapplication.data.Photo
import com.isanechek.myapplication.data.models.Banner
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.base.BaseScreen
import com.isanechek.myapplication.utils.ImageManager
import com.isanechek.myapplication.utils.loging.DebugContract
import kotlinx.android.synthetic.main.dashboard_screen_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class DashboardScreen : BaseScreen() {

    private val db: DbContract by inject()
    private val debug: DebugContract by inject()

    override fun layoutId(): Int = _layout.dashboard_screen_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        handleSetupUi()
    }

    override fun onDestroy() {
        if (banner_view != null) {
            banner_view.removeHandler()
        }
        super.onDestroy()
    }

    private val bannerClickListener = object : OnBannerClickListener<Banner> {
        override fun onBannerClick(view: View, position: Int, model: Banner) {
            goToScree(_id.go_from_dashboard_to_photos)
        }
    }

    private fun handleSetupUi() {
//        banner_view.apply {
//            delayTime = 2000
//            dotsSelector = _drawable.banner
//            pageNumViewBottomMargin = 16
//            pageNumViewLeftMargin = 16
//            pageNumViewTopMargin = 16
//            pageNumViewRightMargin = 16
//            imageLoaderManager = ImageManager()
//            onBannerClickListener = bannerClickListener
//        }
//            .initPageNumView()
//            .initTips()
//            .resource(loadData())

        // implementation click listener
        loadData()

        sq_top_left.onClick {
            goToScree(_id.go_from_dashboard_to_info)
        }

        sq_bottom_left.onClick {
            goToScree(_id.go_from_dashboard_to_market)
        }

        sq_bottom_right.onClick {
            goToScree(_id.go_from_dashboard_to_about)
        }

        sq_top_right.onClick {
            goToScree(_id.go_from_dashboard_to_photos)
        }
    }


    private fun loadData(): MutableList<Banner> {
        val temp = mutableListOf<Banner>()
        GlobalScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.Default) {
                db.getPhotos().map { it.transform() }.toList()
            }
            debug.log("Cache size ${result.size}")
            if (result.isNotEmpty()) {
                banner_view.apply {
                    delayTime = 2000
                    pageNumViewBottomMargin = 16
                    pageNumViewLeftMargin = 16
                    pageNumViewTopMargin = 16
                    pageNumViewRightMargin = 16
                    imageLoaderManager = ImageManager()
                    onBannerClickListener = bannerClickListener
                }
                    .initPageNumView()
                    .initTips()
                    .resource(result.toMutableList())
            }


        }
        return temp
    }

    private fun Photo.transform(): Banner = Banner(
        image = this.smallUrl,
        title = ""
    )
}