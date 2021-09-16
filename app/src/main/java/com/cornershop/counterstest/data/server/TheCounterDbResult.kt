package com.cornershop.counterstest.data.server


import com.google.gson.annotations.SerializedName

data class TheCounterDbResult(
    @SerializedName("count")
    val count: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)