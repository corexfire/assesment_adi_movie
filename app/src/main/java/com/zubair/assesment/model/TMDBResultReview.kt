package com.zubair.assesment.model

import com.google.gson.annotations.SerializedName

data class TMDBResultReview(
    @SerializedName("id")
    val id: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("url")
    val url: String
)