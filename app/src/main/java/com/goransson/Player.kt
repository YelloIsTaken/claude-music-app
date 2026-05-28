package com.goransson

import android.content.Context
import android.media.MediaPlayer

object Player {

    private var mediaPlayer: MediaPlayer? = null

    var currentSongTitle = ""
    var isPlaying = false

    fun play(context: Context, song: Song) {
        stop()
        val resId = context.resources.getIdentifier(song.rawResName, "raw", context.packageName)
        if (resId == 0) return

        currentSongTitle = song.title
        isPlaying = true

        mediaPlayer = MediaPlayer.create(context.applicationContext, resId)?.apply {
            setOnCompletionListener {
                isPlaying = false
                currentSongTitle = ""
                release()
                mediaPlayer = null
            }
            start()
        }
    }

    fun stop() {
        runCatching { mediaPlayer?.stop() }
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
        currentSongTitle = ""
    }
}
