package com.isanechek.myapplication.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.sqlite.transaction
import com.isanechek.myapplication.data.entity.MarketEntity
import com.isanechek.myapplication.data.models.market.MarketItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DbContract {
    fun getItem(id: Int) : MarketEntity
    fun getItems(): List<MarketEntity>
    fun insertItem(market: MarketItem)
}

class DatabaseHandler(
    context: Context
) : DbContract, SQLiteOpenHelper(
    context,
    DatabaseHandler.DB_NAME,
    null,
    DatabaseHandler.DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_MARKET = "CREATE TABLE ${MarketEntity.TABLE_NAME} (" +
                "${MarketEntity.COLUMN_ID} $DB_COLUMN_INTEGER, " +
                "${MarketEntity.COLUMN_OWNER_ID} $DB_COLUMN_INTEGER, " +
                "${MarketEntity.COLUMN_TITLE} $DB_COLUMN_TEXT, " +
                "${MarketEntity.COLUMN_COVER_URL} $DB_COLUMN_TEXT, " +
                "${MarketEntity.COLUMN_PRICE} $DB_COLUMN_TEXT);"
        db?.execSQL(CREATE_TABLE_MARKET)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_MARKET = "DROP TABLE IF EXISTS ${MarketEntity.TABLE_NAME}"
        db?.execSQL(DROP_TABLE_MARKET)
        onCreate(db)
    }

    override fun getItem(id: Int) : MarketEntity {
        val db = this@DatabaseHandler.writableDatabase
        val select = "SELECT * FROM ${MarketEntity.TABLE_NAME} WHERE ${MarketEntity.COLUMN_ID} = $id"
        val cursor = db.rawQuery(select, null)
        cursor?.moveToFirst()
        val _id = cursor.getInt(cursor.getColumnIndex(MarketEntity.COLUMN_ID))
        val _ownerId = cursor.getInt(cursor.getColumnIndex(MarketEntity.COLUMN_OWNER_ID))
        val _title = cursor.getString(cursor.getColumnIndex(MarketEntity.COLUMN_TITLE))
        val _url = cursor.getString(cursor.getColumnIndex(MarketEntity.COLUMN_COVER_URL))
        val _price = cursor.getString(cursor.getColumnIndex(MarketEntity.COLUMN_PRICE))
        cursor.close()
        db.close()
        return MarketEntity(
            id = _id,
            ownerId = _ownerId,
            title = _title,
            coverUrl = _url,
            price = _price
        )
    }

    override fun getItems(): List<MarketEntity> {
        val temp = mutableListOf<MarketEntity>()
        val db = this@DatabaseHandler.writableDatabase
        val select = "SELECT * FROM ${MarketEntity.TABLE_NAME}"
        val cursor = db.rawQuery(select, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val _id = cursor.getInt(cursor.getColumnIndex(MarketEntity.COLUMN_ID))
                    val _ownerId = cursor.getInt(cursor.getColumnIndex(MarketEntity.COLUMN_OWNER_ID))
                    val _title = cursor.getString(cursor.getColumnIndex(MarketEntity.COLUMN_TITLE))
                    val _url = cursor.getString(cursor.getColumnIndex(MarketEntity.COLUMN_COVER_URL))
                    val _price = cursor.getString(cursor.getColumnIndex(MarketEntity.COLUMN_PRICE))
                    temp.add(
                        MarketEntity(
                            id = _id,
                            ownerId = _ownerId,
                            title = _title,
                            coverUrl = _url,
                            price = _price
                        )
                    )
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return temp
    }

    override fun insertItem(market: MarketItem) {
        val db = this@DatabaseHandler.writableDatabase
        db.transaction {
            addItem(market, db)
        }
    }

    private fun addItem(market: MarketItem, db: SQLiteDatabase) {
        val v = ContentValues()
        with(v) {
            put(MarketEntity.COLUMN_ID, market.id)
            put(MarketEntity.COLUMN_OWNER_ID, market.ownerId)
            put(MarketEntity.COLUMN_TITLE, market.title)
            put(MarketEntity.COLUMN_PRICE, market.price.text)
            put(MarketEntity.COLUMN_COVER_URL, market.coverUrl)
        }
        db.insert(MarketEntity.TABLE_NAME, null, v)
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "Tosno.db"
        private const val DB_COLUMN_INTEGER = "INTEGER"
        private const val DB_COLUMN_TEXT = "TEXT"
    }
}