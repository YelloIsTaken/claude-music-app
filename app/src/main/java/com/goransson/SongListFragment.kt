package com.goransson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.goransson.databinding.FragmentSongListBinding

class SongListFragment : Fragment() {

    private var _binding: FragmentSongListBinding? = null
    private val binding get() = _binding!!
    private lateinit var songAdapter: SongAdapter
    private lateinit var movie: Movie

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = requireArguments().getInt(ARG_MOVIE_ID)
        movie = MusicData.movies.first { it.id == movieId }

        setupMoviePanel()
        setupSongList()

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
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
                MusicService.stop(requireContext())
                songAdapter.updateCurrentSong("")
            } else {
                MusicService.play(requireContext(), song, movie)
                songAdapter.updateCurrentSong(song.title)
            }
        }

        binding.recyclerSongs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        if (::songAdapter.isInitialized) {
            songAdapter.updateCurrentSong(MusicService.currentSongTitle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_MOVIE_ID = "movie_id"

        fun newInstance(movieId: Int) = SongListFragment().apply {
            arguments = Bundle().apply { putInt(ARG_MOVIE_ID, movieId) }
        }
    }
}
