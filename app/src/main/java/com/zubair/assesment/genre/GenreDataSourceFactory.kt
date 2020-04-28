package com.zubair.assesment.genre

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.model.TMDBThumb
import io.reactivex.disposables.CompositeDisposable

class GenreDataSourceFactory(
    private val apiService: TMDBInterface,
    private val compositeDisposable: CompositeDisposable,
    private val genreId: String,
    private val sort_by: String
) : DataSource.Factory<Int, TMDBThumb>() {

    val genreLiveDataSource = MutableLiveData<GenreDataSource>()

    override fun create(): DataSource<Int, TMDBThumb> {
        val genreDataSource = GenreDataSource(apiService, compositeDisposable, genreId, sort_by)
        genreLiveDataSource.postValue(genreDataSource)
        return genreDataSource
    }
}