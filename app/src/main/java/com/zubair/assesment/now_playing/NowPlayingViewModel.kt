package com.zubair.assesment.now_playing

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.zubair.assesment.api.PER_PAGE
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBThumb
import com.zubair.assesment.network.NetworkState
import io.reactivex.disposables.CompositeDisposable


class NowPlayingViewModel(private val apiService: TMDBInterface) : ViewModel() {

    private lateinit var nowPlayingPagedList: LiveData<PagedList<TMDBThumb>>
    private lateinit var nowPlayingDataSourceFactory: NowPlayingDataSourceFactory
    private val compositeDisposable = CompositeDisposable()

    fun nowPlayingView(): LiveData<PagedList<TMDBThumb>> {
        nowPlayingDataSourceFactory = NowPlayingDataSourceFactory(apiService, compositeDisposable)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        nowPlayingPagedList = LivePagedListBuilder(nowPlayingDataSourceFactory, config).build()
        return nowPlayingPagedList
    }

    fun nowPlayingNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<NowPlayingDataSource, NetworkState>(
            nowPlayingDataSourceFactory.nowPlayingLiveDataSource, NowPlayingDataSource::networkState
        )
    }

    fun listIsEmpty(): Boolean {
        return nowPlayingPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}