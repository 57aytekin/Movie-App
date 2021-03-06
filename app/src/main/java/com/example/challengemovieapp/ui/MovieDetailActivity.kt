package com.example.challengemovieapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.challengemovieapp.R
import com.example.challengemovieapp.data.entities.Movie
import com.example.challengemovieapp.data.repository.NetworkState
import com.example.challengemovieapp.databinding.ActivityMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<MovieDetailViewModel>()
    private lateinit var binding: ActivityMovieDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getStringExtra(MOVIE_ID)

        viewModel.movieDetail(movieId!!).observe(this, Observer {movieDetail ->
            updateUi(movieDetail)
        })
        viewModel.networkState.observe(this, Observer {
            binding.apply {
                detailProgressBar.visibility = if (it == NetworkState.LOADING) View.VISIBLE else View.GONE
                lnDescription.visibility = if (it == NetworkState.LOADED) View.VISIBLE else View.GONE
                tvDetailError.visibility = if (it == NetworkState.ERROR) View.VISIBLE else View.GONE
            }
        })
    }

    private fun updateUi(movieDetail: Movie?) {
        binding.apply {
            Glide.with(this@MovieDetailActivity)
                .load(movieDetail!!.Poster)
                .into(ivDetailImage)
            tvDetailYear.text = movieDetail.Year
            tvDetailTitle.text = movieDetail.Title
            tvDetailRunTime.text = movieDetail.Runtime
            tvDetailReleased.text = movieDetail.Released
            tvDetailRating.text = movieDetail.imdbRating
            tvDetailPlot.text = movieDetail.Plot
            tvDetailGenre.text = movieDetail.Genre
            tvDetailDirector.text = movieDetail.Director
            tvDetailActors.text = movieDetail.Actors
        }
    }
}