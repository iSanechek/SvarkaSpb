package com.isanechek.myapplication.data.transformers.market

import com.isanechek.myapplication.data.models.Likes
import com.isanechek.myapplication.data.models.Size
import com.isanechek.myapplication.data.models.market.*
import com.isanechek.myapplication.data.models.market.media.MarketPhoto
import com.isanechek.myapplication.data.transformers.Transformer
import com.isanechek.myapplication.utils.loging.DebugContract
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException

class MarketDetailItemTransformer(
    private val debug: DebugContract
) : Transformer<List<MarketDetailItem>> {

    override fun transform(source: String): List<MarketDetailItem> {
        val temp = mutableListOf<MarketDetailItem>()
        try {
            val response = JSONObject(source).getJSONObject("response")
            val count = response.getInt("count")
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
                val favorite = item.getBoolean("is_favorite")
                val photos = parsePhotos(item.getJSONArray("photos"))
                val canComment = item.getInt("can_comment")
                val canRepost = item.getInt("can_repost")
                val likes = parseLikes(item.getJSONObject("likes"))
                val viewsCount = item.getInt("views_count")
                temp.add(
                    MarketDetailItem(
                        id = id,
                        ownerId = ownerId,
                        title = title,
                        description = description,
                        price = price,
                        category = category,
                        date = date,
                        coverUrl = coverUrl,
                        availability = availability,
                        favorite = favorite,
                        photos = photos,
                        canComment = canComment,
                        canRepost = canRepost,
                        likes = likes,
                        viewsCont = viewsCount
                    )
                )

            }

        } catch (err: ParseException) {
            debug.sendStackTrace("Parse Json Market Item Error!", err)
        }
        debug.log("Parse Market Items Size ${temp.size}")
        return temp
    }

    private fun parseCurrency(jo: JSONObject) : MarketItemCurrency {
        val id = jo.getInt("id")
        val name = jo.getString("name")
        return MarketItemCurrency(id, name)
    }

    private fun parsePrice(jo: JSONObject) : MarketItemPrice {
        val amount = jo.getInt("amount")
        val text = jo.getString("text")
        val currency = parseCurrency(jo.getJSONObject("currency"))
        return MarketItemPrice(amount, text, currency)
    }

    /*Need rewrite! Duplicate!!! See parseCurrency*/
    private fun parseSection(jo: JSONObject) : MarketItemCategorySection {
        val id = jo.getInt("id")
        val name = jo.getString("name")
        return MarketItemCategorySection(id, name)
    }

    private fun parseCategory(jo: JSONObject) : MarketItemCategory {
        val id = jo.getInt("id")
        val name = jo.getString("name")
        val section = parseSection(jo.getJSONObject("section"))
        return MarketItemCategory(id, name, section)
    }

    private fun parsePhotos(ja: JSONArray): List<MarketPhoto> {
        val temp = mutableListOf<MarketPhoto>()

        for (i in 0 until ja.length()) {
            val jo = ja[i] as JSONObject
            val id = jo.getInt("id")
            val albumId = jo.getInt("album_id")
            val ownerId = jo.getInt("owner_id")
            val userId = jo.getInt("user_id")
            val sizes = parseSizes(jo.getJSONArray("sizes"))
            val text = jo.getString("text")
            val date = jo.getLong("date")
            temp.add(
                MarketPhoto(
                    id = id,
                    albumId = albumId,
                    ownerId = ownerId,
                    userId = userId,
                    text = text,
                    date = date,
                    sizeCovers = sizes
                )
            )
        }

        return temp
    }

    private fun parseSizes(ja: JSONArray) : List<Size> {
        val temp = mutableListOf<Size>()
        for (i in 0 until ja.length()) {
            val item = ja[i] as JSONObject
            val type = item.getString("type")
            val url = item.getString("url")
            val width = item.getInt("width")
            val height = item.getInt("height")
            temp.add(
                Size(
                    type,
                    url,
                    width,
                    height
                )
            )
        }
        return temp
    }

    private fun parseLikes(jo: JSONObject) : Likes {
        val userLikes = jo.getInt("user_likes")
        val count = jo.getInt("count")
        return Likes(userLikes, count)
    }
}