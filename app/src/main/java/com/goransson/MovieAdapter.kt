package com.goransson

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goransson.databinding.ItemMovieBinding

class MovieAdapter(
    private val movies: List<Movie>,
    private val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    inner class ViewHolder(private val b: ItemMovieBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(movie: Movie) {
            b.textTitle.text        = movie.title
            b.textYear.text         = movie.year.toString()
            b.textDescription.text  = movie.description
            b.cardRoot.setCardBackgroundColor(movie.color)
            b.textTitle.setTextColor(movie.accentColor)
            b.root.setOnClickListener { onClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(movies[position])

    override fun getItemCount() = movies.size
}
