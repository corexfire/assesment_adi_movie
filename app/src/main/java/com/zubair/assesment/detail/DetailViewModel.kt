package com.zubair.assesment.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zubair.assesment.model.TMDBDetail
import com.zubair.assesment.network.NetworkState
import io.reactivex.disposables.CompositeDisposable


class DetailViewModel(private val detailRepository: DetailRepository, movieId: Int) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val detailMovie: LiveData<TMDBDetail> by lazy {
        detailRepository.getDetailMovie(compositeDisposable, movieId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        detailRepository.getDetailNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}