package com.zubair.assesment

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.zubair.assesment.adapter.PagedListRVAdapter
import com.zubair.assesment.api.TMDBClient
import com.zubair.assesment.api.TMDBInterface
import com.zubair.assesment.detail.DetailRepository
import com.zubair.assesment.detail.DetailViewModel
import com.zubair.assesment.genre.GenreViewModel
import com.zubair.assesment.model.TMDBDetail
import com.zubair.assesment.network.NetworkState
import com.zubair.assesment.now_playing.NowPlayingViewModel
import com.zubair.assesment.search.SearchViewModel
import kotlinx.android.synthetic.main.main_content.*
import kotlinx.android.synthetic.main.main_drawer.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val apiService: TMDBInterface = TMDBClient.getClient()


    private lateinit var searchViewModel: SearchViewModel
    private var searchView: SearchView? = null

    private lateinit var genreViewModel: GenreViewModel
    private lateinit var nowPlayingViewModel: NowPlayingViewModel


    private lateinit var detailViewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository

    private var firstTime: Long = 0
    private var secondTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_drawer)


        main_drawer_navigationView.setNavigationItemSelectedListener(this)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true) //드로어를 꺼낼 홈 버튼 활성화
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp) //홈버튼 이미지 변경

        nowPlaying()
    }

    private fun nowPlaying() {
        supportActionBar!!.title = "Movies"
        nowPlayingViewModel = getNowPlayingViewModel()
        val nowPlayingAdapter = PagedListRVAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = nowPlayingAdapter.getItemViewType(position)
                return if (viewType == nowPlayingAdapter.MOVIE_TYPE) 1 else 3
            }
        }
        movie_recyclerView.layoutManager = gridLayoutManager
        movie_recyclerView.setHasFixedSize(true)
        movie_recyclerView.adapter = nowPlayingAdapter

        nowPlayingViewModel.nowPlayingView().observe(this, Observer {
            nowPlayingAdapter.submitList(it)
        })
        nowPlayingViewModel.nowPlayingNetworkState().observe(this, Observer {
            movie_progress_bar.visibility =
                if (nowPlayingViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            movie_error_text.visibility =
                if (nowPlayingViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!nowPlayingViewModel.listIsEmpty()) {
                nowPlayingAdapter.setNetworkState(it)
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nowPlaying_movie -> {
                nowPlaying()
            }
            R.id.genre_popular_movie -> {
                val items = arrayOf(
                    "Action",
                    "Adventure",
                    "Animation",
                    "Comedy",
                    "Crime",
                    "Documentary",
                    "Drama",
                    "Family",
                    "Fantasy",
                    "SF",
                    "Horror",
                    "Thriller",
                    "Mystery",
                    "War",
                    "Romance"
                )
                AlertDialog.Builder(this, 3)
                    .setTitle("Select Genre")
                    .setItems(items) { dialog, which ->
                        when (which) {
                            0 -> genreId("28", "Action", "popularity.desc")
                            1 -> genreId("12", "Adventure", "popularity.desc")
                            2 -> genreId("16", "Animation", "popularity.desc")
                            3 -> genreId("35", "Comedy", "popularity.desc")
                            4 -> genreId("80", "Crime", "popularity.desc")
                            5 -> genreId("99", "Documentary", "popularity.desc")
                            6 -> genreId("18", "Drama", "popularity.desc")
                            7 -> genreId("10751", "Family", "popularity.desc")
                            8 -> genreId("14", "Fantasy", "popularity.desc")
                            9 -> genreId("878", "SF", "popularity.desc")
                            10 -> genreId("27", "Horror", "popularity.desc")
                            11 -> genreId("53", "Thriller", "popularity.desc")
                            12 -> genreId("9648", "Mystery", "popularity.desc")
                            13 -> genreId("10752", "War", "popularity.desc")
                            14 -> genreId("10749", "Romance", "popularity.desc")
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }


        }
        main_drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun bindUI(it: TMDBDetail) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                main_drawer.openDrawer(GravityCompat.START)
            }
            R.id.search_movie -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun genreId(genreId: String, genreName: String, sort_by: String) {
        supportActionBar!!.title = "Genre- $genreName"

        genreViewModel = getGenreViewModel()
        val genreAdapter = PagedListRVAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = genreAdapter.getItemViewType(position)
                return if (viewType == genreAdapter.MOVIE_TYPE) 1 else 3
            }
        }
        movie_recyclerView.layoutManager = gridLayoutManager
        movie_recyclerView.setHasFixedSize(true)
        movie_recyclerView.adapter = genreAdapter

        genreViewModel.getGenreView(genreId, sort_by).observe(this, Observer {
            genreAdapter.submitList(it)
        })
        genreViewModel.genreNetworkState().observe(this, Observer {
            movie_progress_bar.visibility =
                if (genreViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            movie_error_text.visibility =
                if (genreViewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!genreViewModel.listIsEmpty()) {
                genreAdapter.setNetworkState(it)
            }
        })
    }

    fun searchQuery(query: String) {
        supportActionBar!!.title = "Cari: $query"

        searchViewModel = getSearchViewModel()
        val searchAdapter = PagedListRVAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType: Int = searchAdapter.getItemViewType(position)
                return if (viewType == searchAdapter.MOVIE_TYPE) 1 else 3
            }
        }
        movie_recyclerView.layoutManager = gridLayoutManager
        movie_recyclerView.setHasFixedSize(true)
        movie_recyclerView.adapter = searchAdapter

        searchViewModel.searchView(query).observe(this, Observer {
            searchAdapter.submitList(it)
        })
        searchViewModel.searchViewNetworkState().observe(this, Observer {
            movie_progress_bar.visibility =
                if (searchViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            if (!searchViewModel.listIsEmpty()) {
                searchAdapter.setNetworkState(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_item, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem: MenuItem? = menu?.findItem(R.id.search_movie)
        searchView = searchItem?.actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Integer.MAX_VALUE
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery(newText.toString())
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
        return true
    }


    private fun getSearchViewModel(): SearchViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SearchViewModel(apiService) as T
            }
        })[SearchViewModel::class.java]
    }

    private fun getGenreViewModel(): GenreViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return GenreViewModel(apiService) as T
            }
        })[GenreViewModel::class.java]
    }

    private fun getNowPlayingViewModel(): NowPlayingViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NowPlayingViewModel(apiService) as T
            }

        })[NowPlayingViewModel::class.java]
    }

    private fun getDetailViewModel(movieId: Int): DetailViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DetailViewModel(detailRepository, movieId) as T
            }
        })[DetailViewModel::class.java]
    }

    override fun onBackPressed() {
        if (!searchView!!.isIconified) {
            searchView!!.isIconified = true
            searchView!!.onActionViewCollapsed()
        } else if (main_drawer.isDrawerOpen(GravityCompat.START)) {
            main_drawer.closeDrawer(GravityCompat.START)
        } else {
            secondTime = System.currentTimeMillis()
            if (secondTime - firstTime < 2000) {
                super.onBackPressed()
                finishAffinity()
            } else Toast.makeText(this, "한 번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show()
            firstTime = System.currentTimeMillis()
        }
    }
}