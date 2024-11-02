package com.example.cinemagic.HomeScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinemagic.HomeScreen.data.HomeRepo
import com.example.cinemagic.R

class HomeScreenAdapter(private val onMovieClick: (HomeRepo) -> Unit) :
    RecyclerView.Adapter<HomeScreenAdapter.MovieViewHolder>() {

    private var movies: List<HomeRepo> = emptyList()

    fun submitList(movies: List<HomeRepo>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(itemView: View, private val onMovieClick: (HomeRepo) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        private val movieImage: ImageView = itemView.findViewById(R.id.movie_image)

        fun bind(movie: HomeRepo) {
            movieTitle.text = movie.title
            Glide.with(itemView.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .into(movieImage)
            itemView.setOnClickListener { onMovieClick(movie) }
        }
    }
}
