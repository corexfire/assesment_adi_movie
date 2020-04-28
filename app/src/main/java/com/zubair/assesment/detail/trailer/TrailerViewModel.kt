package com.zubair.assesment.detail.trailer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zubair.assesment.model.TMDBVideoResponse
import io.reactivex.disposables.CompositeDisposable

class TrailerViewModel(private val trailerRepository: TrailerRepository, movieId: Int) :
    ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val trailerMovie: LiveData<TMDBVideoResponse> by lazy {
        trailerRepository.getCreditsMovie(compositeDisposable, movieId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
