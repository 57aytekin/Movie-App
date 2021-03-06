package com.example.challengemovieapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.paging.LoadState
import com.example.challengemovieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
const val MOVIE_ID = "movie_id"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MovieAdapter.OnSearchItemClickListener {
    private val viewModel by viewModels<MovieViewModel>()
    private lateinit var  binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter  = MovieAdapter(this)
        viewModel.searchList.observe(this){
            adapter.submitData(lifecycle, it)
        }

        binding.apply {
            recyclerView.adapter = adapter
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBarMain.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1){
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                }else{
                    textViewEmpty.isVisible = false
                }
            }
        }
        //Search
        binding.apply {
            ivAramaSearchIcon.setOnClickListener {
                searchKeyword(etSearchText.text.toString())
            }
            etSearchText.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchKeyword(etSearchText.text.toString())
                }
                true
            }
        }
    }


    private fun searchKeyword(keyword : String) {
        if (keyword.isNotEmpty()){
            viewModel.searchMovie(keyword)
            binding.apply {
                etSearchText.hideKeyboard()
                etSearchText.clearFocus()
                recyclerView.scrollToPosition(0)
            }
        }
    }
    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onItemClick(movieId: String) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra(MOVIE_ID, movieId)
        startActivity(intent)
    }
}