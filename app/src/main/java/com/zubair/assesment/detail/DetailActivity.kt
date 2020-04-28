package com.zubair.assesment.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zubair.assesment.R
import com.zubair.assesment.api.POSTER_URL
import com.zubair.assesment.api.TMDBClient
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.detail.review.ReviewRVAdapter
import com.zubair.assesment.detail.review.ReviewRepository
import com.zubair.assesment.detail.review.ReviewViewModel
import com.zubair.assesment.detail.trailer.TrailerRVAdapter
import com.zubair.assesment.detail.trailer.TrailerRepository
import com.zubair.assesment.detail.trailer.TrailerViewModel
import com.zubair.assesment.model.*
import com.zubair.assesment.network.NetworkState
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.DecimalFormat


class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository
    private lateinit var trailerViewModel: TrailerViewModel
    private lateinit var trailerRepository: TrailerRepository
    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var reviewRepository: ReviewRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar!!.title = "Movie Details"
        val movieId: Int = intent.getIntExtra("id", 1)
        val apiService: TMDBInterface = TMDBClient.getClient()

        detailRepository = DetailRepository(apiService)
        detailViewModel = getDetailViewModel(movieId)
        detailViewModel.detailMovie.observe(this, Observer {
            bindUI(it)
            setGenreRVAdapter(it.genres)
            setProductionRVAdapter(it.production_countries)
        })
        detailViewModel.networkState.observe(this, Observer {
            detail_progress_bar.visibility =
                if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
            detail_error_text.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })

        trailerRepository = TrailerRepository(apiService)
        trailerViewModel = getTrailerViewModel(movieId)
        trailerViewModel.trailerMovie.observe(this, Observer {
            setTrailerRVAdapter(it.results)
        })

        reviewRepository = ReviewRepository(apiService)
        reviewViewModel = getReviewViewModel(movieId)
        reviewViewModel.trailerMovie.observe(this, Observer {
            setReviewRVAdapter(it.results)
        })


    }

    private fun getReviewViewModel(movieId: Int): ReviewViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ReviewViewModel(reviewRepository, movieId) as T
            }
        })[ReviewViewModel::class.java]
    }

    private fun setReviewRVAdapter(item: ArrayList<TMDBResultReview>) {
        val creditsRVAdapter = ReviewRVAdapter(item)

        crew_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        crew_recyclerView.setHasFixedSize(true)
        crew_recyclerView.adapter = creditsRVAdapter
    }

    private fun setTrailerRVAdapter(item: ArrayList<TMDBResult>) {
        val creditsRVAdapter = TrailerRVAdapter(item)

        credits_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        credits_recyclerView.setHasFixedSize(true)
        credits_recyclerView.adapter = creditsRVAdapter
    }

    private fun getTrailerViewModel(movieId: Int): TrailerViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return TrailerViewModel(trailerRepository, movieId) as T
            }
        })[TrailerViewModel::class.java]
    }

    private fun bindUI(it: TMDBDetail) {
        val decimalFormat = DecimalFormat("###,###")
        val decimalBudget = "$" + decimalFormat.format(it.budget)
        val decimalRevenue = "$" + decimalFormat.format(it.revenue)
        val runtime = it.runtime.toString() + "minute"
        val originalTitle = it.title + "\n(${it.original_title})"

        detail_movie_title.text = originalTitle
        detail_movie_tagline.text = it.tagline
        detail_movie_release.text = it.releaseDate
        detail_movie_voteCount.text = it.vote_count.toString()
        detail_movie_rating.text = it.rating.toString()
        detail_movie_runtime.text = runtime
        detail_movie_budget.text = decimalBudget
        detail_movie_revenue.text = decimalRevenue
        detail_movie_overview.text = it.overview

        val moviePosterURL: String = POSTER_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(detail_movie_poster)
    }

    private fun setGenreRVAdapter(item: ArrayList<Genres>) {
        val genreRVAdapter = GenreRVAdapter(item)
        genre_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        genre_recyclerView.setHasFixedSize(true)
        genre_recyclerView.adapter = genreRVAdapter
    }


    private fun getDetailViewModel(movieId: Int): DetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(detailRepository, movieId) as T
            }
        })[DetailViewModel::class.java]
    }

    private fun setProductionRVAdapter(item: ArrayList<Production>) {
        val productionRVAdapter = ProductionRVAdapter(item)
        production_recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        production_recyclerView.setHasFixedSize(true)
        production_recyclerView.adapter = productionRVAdapter
    }

}
