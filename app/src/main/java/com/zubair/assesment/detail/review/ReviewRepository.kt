package com.zubair.assesment.detail.review

import androidx.lifecycle.LiveData
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBReviewResponse
import io.reactivex.disposables.CompositeDisposable

class ReviewRepository(private val apiService: TMDBInterface) {
    private lateinit var reviewDataSource: ReviewDataSource

    fun getReviewMovie(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<TMDBReviewResponse> {
        reviewDataSource = ReviewDataSource(
            apiService,
            compositeDisposable
        )
        reviewDataSource.getReviewMovie(movieId)
        return reviewDataSource.trailerMovieResponse
    }
}