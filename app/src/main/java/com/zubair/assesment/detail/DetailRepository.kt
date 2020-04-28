package com.zubair.assesment.detail

import androidx.lifecycle.LiveData
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBDetail
import com.zubair.assesment.network.NetworkState
import io.reactivex.disposables.CompositeDisposable


class DetailRepository(private val apiService: TMDBInterface) {

    lateinit var detailDataSource: DetailDataSource

    fun getDetailMovie(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<TMDBDetail> {
        detailDataSource = DetailDataSource(apiService, compositeDisposable)
        detailDataSource.getSelectedMovieDetails(movieId)

        return detailDataSource.selectedMovieResponse
    }

    fun getDetailNetworkState(): LiveData<NetworkState> {
        return detailDataSource.networkState
    }
}