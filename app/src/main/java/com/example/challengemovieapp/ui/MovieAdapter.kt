package com.example.challengemovieapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.challengemovieapp.data.entities.Search
import com.example.challengemovieapp.databinding.ItemMovieBinding

class MovieAdapter(private val listener : OnSearchItemClickListener) :
    PagingDataAdapter<Search, MovieAdapter.MovieViewHolder>(SEARCH_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null){
            holder.bind(currentItem)
        }
    }

    inner class MovieViewHolder(private val binding : ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val item = getItem(position)
                    if (item != null){
                        listener.onItemClick(item.imdbID)
                    }
                }
            }
        }

        fun bind(search: Search){
            binding.apply {
                Glide.with(itemView).load(search.Poster)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
                textView.text = search.Title
            }
        }
    }

    interface OnSearchItemClickListener{
        fun onItemClick(movieId : String)
    }

    companion object {
        private val SEARCH_COMPARATOR = object : DiffUtil.ItemCallback<Search>() {
            override fun areItemsTheSame(oldItem: Search, newItem: Search): Boolean =
                oldItem.imdbID == newItem.imdbID

            override fun areContentsTheSame(oldItem: Search, newItem: Search): Boolean =
                oldItem == newItem

        }
    }
}