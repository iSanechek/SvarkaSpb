package com.isanechek.myapplication.data.models

import com.bannerlayout.BannerModelCallBack

class Banner(image: Any, title: String) : BannerModelCallBack {
    override val bannerUrl: String = image.toString()
    override val bannerTitle: String = title
}