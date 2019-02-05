package com.isanechek.myapplication.di

import com.isanechek.myapplication.data.models.market.Market
import com.isanechek.myapplication.data.transformers.MarketAllItemsTransformer
import com.isanechek.myapplication.data.transformers.Transformer
import org.koin.dsl.module.module

val dataModule = module {

    /*Market*/

    factory<Transformer<Market>>(name = MARKET_ALL_ITEM_TRANSFORMER) {
        MarketAllItemsTransformer(
            get()
        )
    }

}

const val MARKET_ALL_ITEM_TRANSFORMER = "market_all_items_transformer"