package com.isanechek.myapplication.screens.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.bannerlayout.BannerTransformer
import com.bannerlayout.OnBannerClickListener
import com.isanechek.myapplication.*
import com.isanechek.myapplication.data.Photo
import com.isanechek.myapplication.data.models.AboutItem
import com.isanechek.myapplication.data.models.Banner
import com.isanechek.myapplication.data.models.LoadStatus
import com.isanechek.myapplication.screens.base.BaseScreen
import com.isanechek.myapplication.screens.viewer.ViewerScreen
import com.isanechek.myapplication.utils.ImageManager
import com.isanechek.myapplication.utils.PrefManager
import com.isanechek.myapplication.utils.loging.DebugContract
import kotlinx.android.synthetic.main.dashboard_screen_layout.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardScreen : BaseScreen() {

    private val debug: DebugContract by inject()
    private val vm: DashboardViewModel by viewModel()
    private val pref: PrefManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (pref.showLicense) {
            goToScree(_id.go_from_dashboard_to_license)
        }
    }

    override fun layoutId(): Int = _layout.dashboard_screen_layout

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
            findNavController().navigate(
                _id.go_from_dashboard_to_viewer, bundleOf(
                    ViewerScreen.ARGS to model.bannerUrl,
                    ViewerScreen.ARGS_FULL to model.bannerTitle
                )
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

    private fun actionDialogShow(isMessage: Boolean) {
        val list = mutableListOf<AboutItem>()
        if (isMessage) {
            list.add(
                AboutItem(
                    id = SMS_ID,
                    title = "Написать смс",
                    data = "+79312070664",
                    icon = 0
                )
            )
            if (isAppInstalledOrNot(requireActivity(), VIBER_PACKAGE)) {
                list.add(
                    AboutItem(
                        id = SMS_VIBER_ID,
                        title = "Написать в viber",
                        data = "+79312070664",
                        icon = 0
                    )
                )
            }
//            list.add(
//                AboutItem(
//                    id = VK_ID,
//                    title = "Написать в Vk",
//                    data = "",
//                    icon = 0
//                )
//            )
        } else {
            list.add(
                AboutItem(
                    id = CALL_ID,
                    title = "Позвонить",
                    data = "+79312070664",
                    icon = 0
                )
            )
            if (isAppInstalledOrNot(requireActivity(), VIBER_PACKAGE)) {
                list.add(
                    AboutItem(
                        id = CALL_VIBER_ID,
                        title = "Позвонить в viber",
                        data = "+79312070664",
                        icon = 0
                    )
                )
            }
        }

        MaterialDialog(requireActivity()).show {
            listItems(items = list.map {
                it.title
            }.toList()) { dialog, index, _ ->
                val item = list[index]
                debug.log("item click ${item.id}")
                debug.log("item click ${item.title}")
                when (item.id) {
                    SMS_VIBER_ID -> startViber(item.data)
                    CALL_VIBER_ID -> startViber(item.data)
                    SMS_ID -> requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", item.data, null)))
                    CALL_ID -> {
                        debug.log("call intent")
                        val i = Intent(Intent.ACTION_DIAL)
                        i.data = Uri.parse("tel:+79312070664")
                        requireActivity().startActivity(i)
                    }
                }
                dialog.dismiss()
            }
        }
    }

    private fun startViber(number: String) {
        val phoneNumber = "tel:$number"
        Intent(Intent.ACTION_VIEW)
            .run {
                setClassName("com.viber.voip", "com.viber.voip.WelcomeActivity")
                data = phoneNumber.toUri()
                startActivity(this)
            }
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
                delayTime = 5000
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

    companion object {
        private const val SMS_ID = 0
        private const val CALL_ID = 1
        private const val CALL_VIBER_ID = 2
        private const val SMS_VIBER_ID = 3
        private const val VK_ID = 4
    }
}