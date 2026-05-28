package com.goransson

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.goransson.databinding.ActivitySongListBinding

class SongListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySongListBinding
    private lateinit var songAdapter: SongAdapter
    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, 1)
        movie = MusicData.movies.first { it.id == movieId }

        setupMoviePanel()
        setupSongList()
    }

    private fun setupMoviePanel() {
        binding.textMovieTitle.text       = movie.title
        binding.textMovieYear.text        = movie.year.toString()
        binding.textMovieDescription.text = movie.description
        binding.moviePanel.setBackgroundColor(movie.color)
        binding.textMovieTitle.setTextColor(movie.accentColor)
    }

    private fun setupSongList() {
        songAdapter = SongAdapter(movie.songs, MusicService.currentSongTitle) { song ->
            if (MusicService.isPlaying && MusicService.currentSongTitle == song.title) {
                MusicService.stop(this)
                songAdapter.updateCurrentSong("")
            } else {
                MusicService.play(this, song, movie)
                songAdapter.updateCurrentSong(song.title)
            }
        }

        binding.recyclerSongs.apply {
            layoutManager = LinearLayoutManager(this@SongListActivity)
            adapter        = songAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        if (::songAdapter.isInitialized) {
            songAdapter.updateCurrentSong(MusicService.currentSongTitle)
        }
    }

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }
}
