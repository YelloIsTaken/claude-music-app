package com.goransson

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.goransson.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieAdapter = MovieAdapter(MusicData.movies) { movie ->
            startActivity(
                Intent(this, SongListActivity::class.java)
                    .putExtra(SongListActivity.EXTRA_MOVIE_ID, movie.id)
            )
        }

        binding.recyclerMovies.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter        = movieAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        movieAdapter.notifyDataSetChanged()
    }
}
