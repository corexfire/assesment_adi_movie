package com.zubair.assesment.search

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

class SearchViewModel(private var apiService: TMDBInterface) : ViewModel() {

    private lateinit var searchPagedList: LiveData<PagedList<TMDBThumb>>
    private lateinit var searchDataSourceFactory: SearchDataSourceFactory
    private val compositeDisposable = CompositeDisposable()


    fun searchView(searchQuery: String): LiveData<PagedList<TMDBThumb>> {
        searchDataSourceFactory =
            SearchDataSourceFactory(apiService, compositeDisposable, searchQuery)

        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(PER_PAGE)
            .build()
        searchPagedList = LivePagedListBuilder(searchDataSourceFactory, config).build()
        return searchPagedList
    }

    fun searchViewNetworkState(): LiveData<NetworkState> {
        return Transformations.switchMap<SearchDataSource, NetworkState>(
            searchDataSourceFactory.searchLiveDataSource, SearchDataSource::networkState
        )
    }

    fun listIsEmpty(): Boolean {
        return searchPagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}