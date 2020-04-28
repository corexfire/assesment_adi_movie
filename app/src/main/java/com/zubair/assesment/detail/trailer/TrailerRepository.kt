package com.zubair.assesment.detail.trailer

import androidx.lifecycle.LiveData
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBVideoResponse
import io.reactivex.disposables.CompositeDisposable

class TrailerRepository(private val apiService: TMDBInterface) {
    private lateinit var trailerDataSource: TrailerDataSource

    fun getCreditsMovie(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<TMDBVideoResponse> {
        trailerDataSource = TrailerDataSource(
            apiService,
            compositeDisposable
        )
        trailerDataSource.getTrailerMovie(movieId)
        return trailerDataSource.trailerMovieResponse
    }
}