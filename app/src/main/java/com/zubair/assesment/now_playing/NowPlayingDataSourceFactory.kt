package com.zubair.assesment.now_playing

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class NowPlayingDataSourceFactory(
    private val apiService: TMDBInterface,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, TMDBThumb>() {

    val nowPlayingLiveDataSource = MutableLiveData<NowPlayingDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val nowPlayingDataSource = NowPlayingDataSource(apiService, compositeDisposable)
        nowPlayingLiveDataSource.postValue(nowPlayingDataSource)
        return nowPlayingDataSource
    }
}