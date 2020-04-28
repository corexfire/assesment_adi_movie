package com.zubair.assesment.model


import com.google.gson.annotations.SerializedName

data class TMDBVideoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: ArrayList<TMDBResult>
)