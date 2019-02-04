package com.isanechek.myapplication.screens.info

import com.isanechek.myapplication.data.models.Info

object InfoData {

    fun inits() = listOf(
        Info(
            id = 0,
            title = "Аргонодуговая",
            message = "Цветных металлов и сплавов\n(алюминий, титан, медь и т.д.)"
        ),
        Info(
            id = 1,
            title = "Плазменая резка",
            message = "Под давлением(трубы, емкости, резервуары)\nРемонт м/к\nОборудования\nУглы и трубапровод по чертежам и эскизам"
        ),
        Info(
            id = 2,
            title = "Ручная",
            message = ""
        ),
        Info(
            id = 3,
            title = "Полуавтоматическая",
            message = ""
        )
    )
}