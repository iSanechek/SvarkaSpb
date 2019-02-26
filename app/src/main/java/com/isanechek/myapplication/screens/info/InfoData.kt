package com.isanechek.myapplication.screens.info

import com.isanechek.myapplication.data.models.Info

object InfoData {

    fun inits() = listOf(
        Info(
            id = 0,
            title = "Аргонодуговая",
            message = "-Ремонт литых дисков \n" +
                    "-За плавка трещин коллекторов \n" +
                    "-Сварка трещин блоков двигателя \n" +
                    "-Сварка поддонов картера \n" +
                    "-Сварка элементов АКПП и МКПП \n" +
                    "-Сварка нержавеющей стали а так же медь, титан."
        ),
        Info(
            id = 1,
            title = "Плазменая резка",
            message = "-Резание всех видов сталей\n" +
                    "-Алюминияеди\n" +
                    "-Чугуна\n" +
                    "-Титана\n" +
                    "-Листового и профильного проката\n" +
                    "- Осуществлять скос кромок под определенным углом."
        ),
        Info(
            id = 2,
            title = "Ручная",
            message = " Дуговая покрытым электродами —— трубы, емкости, резервуары.\nА так же ремонт Металло конструкций"
        ),
        Info(
            id = 3,
            title = "Полуавтоматическая",
            message = "-Пока нет информации"
        )
    )
}