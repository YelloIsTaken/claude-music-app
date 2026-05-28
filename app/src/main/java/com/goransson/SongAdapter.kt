package com.goransson

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.goransson.databinding.ItemSongBinding

class SongAdapter(
    private val songs: List<Song>,
    private var currentSongTitle: String,
    private val onClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    inner class ViewHolder(private val b: ItemSongBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(song: Song, isPlaying: Boolean) {
            b.textIndex.text     = (adapterPosition + 1).toString()
            b.textSongTitle.text = song.title
            b.textDuration.text  = song.duration

            if (isPlaying) {
                b.root.setBackgroundColor(Color.parseColor("#2A2A2A"))
                b.textSongTitle.setTextColor(Color.WHITE)
                b.indicatorPlaying.visibility = View.VISIBLE
            } else {
                b.root.setBackgroundColor(Color.TRANSPARENT)
                b.textSongTitle.setTextColor(Color.parseColor("#CCCCCC"))
                b.indicatorPlaying.visibility = View.INVISIBLE
            }

            b.root.setOnClickListener { onClick(song) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = songs[position]
        holder.bind(song, MusicService.isPlaying && song.title == currentSongTitle)
    }

    override fun getItemCount() = songs.size

    fun updateCurrentSong(title: String) {
        currentSongTitle = title
        notifyDataSetChanged()
    }
}
