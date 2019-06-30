package model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Account(
    @SerializedName("address")
    val address: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("timestamp")
    val timestamp: BigInteger
) {
    companion object {
        fun fromJson(json: String): Account {
            return Gson().fromJson(json, Account::class.java)
        }
    }
}