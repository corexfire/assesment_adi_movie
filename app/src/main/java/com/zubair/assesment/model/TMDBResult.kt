package com.zubair.assesment.model

import com.google.gson.annotations.SerializedName


data class TMDBResult(
    @SerializedName("id")
    val id: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("site")
    val site: String
)