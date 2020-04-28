package com.zubair.assesment.api

import com.zubair.assesment.model.*
import io.reactivex.Single
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TMDBInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<TMDBDetail>

    @GET("search/movie")
    fun getSearchMovie(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("include_adult") adult: Boolean
    ): Single<TMDBResponse>

    @GET("discover/movie")
    fun getGenrePopularMovie(
        @Query("sort_by") sortBy: String,
        @Query("include_adult") adult: Boolean,
        @Query("page") page: Int,
        @Query("with_genres") genreId: String,
        @Query("vote_count.gte") vote_count: Int
    ): Single<TMDBResponse>

    @GET("discover/movie")
    fun getNowPlayingMovie(@Query("page") page: Int): Single<TMDBResponse>

    @GET("movie/{movie_id}/videos")
    fun getTrailerVideo(
        @Path("movie_id") id: Int
    ): Single<TMDBVideoResponse>

    @GET("movie/{movie_id}/reviews")
    fun getReviewVideo(
        @Path("movie_id") id: Int
    ): Single<TMDBReviewResponse>
}