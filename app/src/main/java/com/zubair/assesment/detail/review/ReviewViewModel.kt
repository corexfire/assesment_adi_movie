package com.zubair.assesment.detail.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zubair.assesment.detail.trailer.TrailerRepository
import com.zubair.assesment.model.TMDBReviewResponse
import com.zubair.assesment.model.TMDBVideoResponse
import io.reactivex.disposables.CompositeDisposable

class ReviewViewModel(private val reviewRepository: ReviewRepository, movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val trailerMovie: LiveData<TMDBReviewResponse> by lazy {
        reviewRepository.getReviewMovie(compositeDisposable, movieId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}