package com.isanechek.myapplication.screens.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bannerlayout.BannerTransformer
import com.bannerlayout.OnBannerClickListener
import com.isanechek.myapplication._id
import com.isanechek.myapplication._layout
import com.isanechek.myapplication.data.Photo
import com.isanechek.myapplication.data.models.Banner
import com.isanechek.myapplication.data.models.LoadStatus
import com.isanechek.myapplication.onClick
import com.isanechek.myapplication.screens.base.BaseScreen
import com.isanechek.myapplication.screens.viewer.ViewerScreen
import com.isanechek.myapplication.setVisible
import com.isanechek.myapplication.utils.ImageManager
import com.isanechek.myapplication.utils.loging.DebugContract
import kotlinx.android.synthetic.main.dashboard_screen_layout2.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardScreen : BaseScreen() {

    private val debug: DebugContract by inject()
    private val vm: DashboardViewModel by viewModel()

    override fun layoutId(): Int = _layout.dashboard_screen_layout2

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        handleSetupUi()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    override fun onDestroy() {
        if (dashboard_banner_view != null) {
            dashboard_banner_view.removeHandler()
        }
        super.onDestroy()
    }

    private val bannerClickListener = object : OnBannerClickListener<Banner> {
        override fun onBannerClick(view: View, position: Int, model: Banner) {
            findNavController().navigate(_id.go_from_dashboard_to_viewer, bundleOf(
                ViewerScreen.ARGS to model.bannerUrl,
                ViewerScreen.ARGS_FULL to model.bannerTitle)
            )
        }
    }

    private fun handleSetupUi() {

        dashboard_work_btn.onClick {
            goToScree(_id.go_from_dashboard_to_info)
        }

        dashboard_shop_btn.onClick {
            goToScree(_id.go_from_dashboard_to_market)
        }

        dashboard_info_btn.onClick {
            goToScree(_id.go_from_dashboard_to_about)
        }

        dashboard_gallery_btn.onClick {
            goToScree(_id.go_from_dashboard_to_photos)
        }

        dashboard_call_action.onClick {
            actionDialogShow(false)
        }

        dashboard_message_action.onClick {
            actionDialogShow(true)
        }
    }

    private fun actionDialogShow(message: Boolean) {

    }

    private fun loadData() {
        vm.loadPhotos()

        vm.loadStatus.observe(this, Observer { status ->
            status ?: return@Observer
            when (status) {
                is LoadStatus.Done -> dashboard_progress.setVisible(false)
                is LoadStatus.Fail -> dashboard_progress.setVisible(false)
                is LoadStatus.Loading -> dashboard_progress.setVisible(true)
            }
        })

        vm.liveData.observe(this, Observer { d ->
            d ?: return@Observer
            dashboard_banner_view.apply {
                delayTime = 2000
                bannerTransformerType = BannerTransformer.ANIMATION_ACCORDION
                imageLoaderManager = ImageManager()
                onBannerClickListener = bannerClickListener
            }
                .resource(d.map { it.transform() }.toMutableList())
                .switchBanner(true)
        })
    }

    private fun Photo.transform(): Banner = Banner(
        image = this.smallUrl,
        title = this.fullUrl
    )
}