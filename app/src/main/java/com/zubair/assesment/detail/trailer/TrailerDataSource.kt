package com.zubair.assesment.detail.trailer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBVideoResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class TrailerDataSource(
    private val apiService: TMDBInterface,
    private val compositeDisposable: CompositeDisposable
) {

    //커스텀 접근자 (getter 커스텀)
    //val 로 선언하면 setter 는 없다
    private val _trailerMovieResponse = MutableLiveData<TMDBVideoResponse>()
    val trailerMovieResponse: LiveData<TMDBVideoResponse>
        get() = _trailerMovieResponse

    fun getTrailerMovie(movieId: Int) {
        try {
            compositeDisposable.add(
                apiService.getTrailerVideo(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _trailerMovieResponse.postValue(it)
                    }, {
                        Log.e("TrailerDataSource", it.message)
                    })
            )
        } catch (e: Exception) {
            Log.e("TrailerDataSource", e.message)
        }
    }
}