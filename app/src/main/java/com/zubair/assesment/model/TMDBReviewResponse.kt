package com.zubair.assesment.model

import com.google.gson.annotations.SerializedName

data class TMDBReviewResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val results: ArrayList<TMDBResultReview>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResult: Int
)