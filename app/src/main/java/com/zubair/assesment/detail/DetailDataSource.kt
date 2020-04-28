package com.zubair.assesment.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBDetail
import com.zubair.assesment.network.NetworkState
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception


class DetailDataSource(
    private val apiService: TMDBInterface,
    private val compositeDisposable: CompositeDisposable
) {

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState

    private val _detailMovieResponse = MutableLiveData<TMDBDetail>()
    val selectedMovieResponse: LiveData<TMDBDetail>
        get() = _detailMovieResponse

    fun getSelectedMovieDetails(movieId: Int) {
        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            _detailMovieResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)

                        }, {
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("DetailDataSource", it.message)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("DetailDataSource", e.message)
        }
    }
}