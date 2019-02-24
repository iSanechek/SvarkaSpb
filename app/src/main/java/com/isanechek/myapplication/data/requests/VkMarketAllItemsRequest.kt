package com.isanechek.myapplication.data.requests

import com.isanechek.myapplication.data.models.market.*
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject
import java.text.ParseException

class VkMarketAllItemsRequest : VKRequest<Market>("market.get") {

    init {
        addParam("owner_id", -125640924)
        addParam("extended", 1)
    }

    override fun parse(r: JSONObject): Market {
        val temp = mutableListOf<MarketItem>()
        var totalSizeItems = 0

        try {

            val response = r.getJSONObject("response")
            totalSizeItems = response.getInt("count")
            val items = response.getJSONArray("items")
            for (i in 0 until items.length()) {
                val item = items[i] as JSONObject
                val id = item.getInt("id")
                val ownerId = item.getInt("owner_id")
                val title = item.getString("title")
                val description = item.getString("description")
                val price = parsePrice(item.getJSONObject("price"))
                val category = parseCategory(item.getJSONObject("category"))
                val date = item.getLong("date")
                val coverUrl = item.getString("thumb_photo")
                val availability = item.getInt("availability")
//                val favorite = item.getBoolean("is_favorite")
                temp.add(
                    MarketItem(
                        id = id,
                        ownerId = ownerId,
                        title = title,
                        description = description,
                        price = price,
                        category = category,
                        date = date,
                        coverUrl = coverUrl,
                        availability = availability,
                        favorite = false
                    )
                )

            }

        } catch (err: ParseException) {

        }

        return Market(totalSizeItems, temp)
    }

    private fun parseCurrency(jo: JSONObject): MarketItemCurrency {
        val id = jo.getInt("id")
        val name = jo.getString("name")
        return MarketItemCurrency(id, name)
    }

    private fun parsePrice(jo: JSONObject): MarketItemPrice {
        val amount = jo.getInt("amount")
        val text = jo.getString("text")
        val currency = parseCurrency(jo.getJSONObject("currency"))
        return MarketItemPrice(amount, text, currency)
    }

    /*Need rewrite! Duplicate!!! See parseCurrency*/
    private fun parseSection(jo: JSONObject): MarketItemCategorySection {
        val id = jo.getInt("id")
        val name = jo.getString("name")
        return MarketItemCategorySection(id, name)
    }

    private fun parseCategory(jo: JSONObject): MarketItemCategory {
        val id = jo.getInt("id")
        val name = jo.getString("name")
        val section = parseSection(jo.getJSONObject("section"))
        return MarketItemCategory(id, name, section)
    }
}