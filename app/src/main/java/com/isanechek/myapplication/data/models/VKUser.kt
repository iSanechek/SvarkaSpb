package com.isanechek.myapplication.data.models

import android.os.Parcelable
import com.isanechek.myapplication.empty
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject

@Parcelize
data class VKUser(val id: Int,
                  val firstName: String,
                  val lastName: String,
                  val photo: String,
                  val deactivated: Boolean) : Parcelable {

    companion object {
        fun empty() = VKUser(0, String.empty(), String.empty(), String.empty(), false)
        fun parse(json: JSONObject) = VKUser(
            id = json.optInt("id", 0),
            firstName = json.optString("first_name", String.empty()),
            lastName = json.optString("last_name", String.empty()),
            photo = json.optString("photo_200", String.empty()),
            deactivated = json.optBoolean("deactivated", false))
    }
}