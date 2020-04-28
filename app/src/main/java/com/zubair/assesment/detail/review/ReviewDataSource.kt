package com.zubair.assesment.detail.review

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBReviewResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class ReviewDataSource(
    private val apiService: TMDBInterface,
    private val compositeDisposable: CompositeDisposable
) {

    //커스텀 접근자 (getter 커스텀)
    //val 로 선언하면 setter 는 없다
    private val _reviewMovieResponse = MutableLiveData<TMDBReviewResponse>()
    val trailerMovieResponse: LiveData<TMDBReviewResponse>
        get() = _reviewMovieResponse

    fun getReviewMovie(movieId: Int) {
        try {
            compositeDisposable.add(
                apiService.getReviewVideo(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        _reviewMovieResponse.postValue(it)
                    }, {
                        Log.e("TrailerDataSource", it.message)
                    })
            )
        } catch (e: Exception) {
            Log.e("TrailerDataSource", e.message)
        }
    }
}