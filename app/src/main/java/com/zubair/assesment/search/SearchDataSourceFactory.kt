package com.zubair.assesment.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable


class SearchDataSourceFactory(
    private val apiService: TMDBInterface,
    private val compositeDisposable: CompositeDisposable,
    private val searchQuery: String
) : DataSource.Factory<Int, TMDBThumb>() {

    val searchLiveDataSource = MutableLiveData<SearchDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val searchDataSource = SearchDataSource(apiService, compositeDisposable, searchQuery)
        searchLiveDataSource.postValue(searchDataSource)
        return searchDataSource
    }
}