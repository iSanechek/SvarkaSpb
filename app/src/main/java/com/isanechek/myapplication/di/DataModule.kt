package com.isanechek.myapplication.di

import com.isanechek.myapplication.data.models.market.Market
import com.isanechek.myapplication.data.models.market.MarketDetailItem
import com.isanechek.myapplication.data.transformers.market.MarketAllItemsTransformer
import com.isanechek.myapplication.data.transformers.Transformer
import com.isanechek.myapplication.data.transformers.market.MarketDetailItemTransformer
import org.koin.dsl.module.module

val dataModule = module {

    /*Market*/

    factory<Transformer<Market>>(name = MARKET_ALL_ITEMS_TRANSFORMER) {
        MarketAllItemsTransformer(
            get()
        )
    }

    factory<Transformer<List<MarketDetailItem>>>(name = MARKET_DETAIL_ITEM_TRANSFORMER) {
        MarketDetailItemTransformer(
            get()
        )
    }

}

const val MARKET_ALL_ITEMS_TRANSFORMER = "market_all_items_transformer"
const val MARKET_DETAIL_ITEM_TRANSFORMER = "market_detail_item_transformer"