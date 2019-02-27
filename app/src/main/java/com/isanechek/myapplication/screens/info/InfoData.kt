package com.isanechek.myapplication.screens.info

import com.isanechek.myapplication.data.models.Info

object InfoData {

    fun inits() = listOf(
        Info(
            id = 0,
            title = "Аргонодуговая",
            message = "-Ремонт литых дисков \n" +
                    "-Заплавка трещин коллекторов \n" +
                    "-Сварка трещин блоков двигателя \n" +
                    "-Сварка поддонов картера \n" +
                    "-Сварка элементов АКПП и МКПП \n" +
                    "-Сварка нержавеющей стали а так же медь, титан."
        ),
        Info(
            id = 1,
            title = "Плазменая резка",
            message = "-Резка всех видов сталей\n" +
                    "-Алюминия\n" +
                    "-Меди" +
                    "-Чугуна\n" +
                    "-Титана\n" +
                    "-Листового и профильного проката\n" +
                    "-Осуществляем скос кромок под определенным углом."
        ),
        Info(
            id = 2,
            title = "Ручная дуговая",
            message = "-Трубы\n" +
                    "-Емкости\n" +
                    "-Резервуары.\n-Так же ремонт металлоконструкций"
        ),
        Info(
            id = 3,
            title = "Полуавтоматическая",
            message = "-Ремонт автомобильных панелей,\n И другие работы с тонким листовым металом"
        )
    )
}