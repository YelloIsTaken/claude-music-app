package com.goransson

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.*
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlin.math.PI
import kotlin.math.sin

class MusicService : Service() {

    private var audioTrack: AudioTrack? = null
    private var playbackThread: Thread? = null
    @Volatile private var keepPlaying = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val songTitle  = intent.getStringExtra(EXTRA_SONG_TITLE)  ?: return START_NOT_STICKY
                val movieTitle = intent.getStringExtra(EXTRA_MOVIE_TITLE) ?: return START_NOT_STICKY
                val frequency  = intent.getDoubleExtra(EXTRA_FREQUENCY, 440.0)
                startPlayback(songTitle, movieTitle, frequency)
            }
            ACTION_STOP -> stopPlayback()
        }
        return START_NOT_STICKY
    }

    private fun startPlayback(songTitle: String, movieTitle: String, frequency: Double) {
        stopAudio()
        currentSongTitle  = songTitle
        currentMovieTitle = movieTitle
        isPlaying         = true
        startForeground(NOTIFICATION_ID, buildNotification(songTitle, movieTitle))
        playAudio(frequency)
    }

    private fun playAudio(frequency: Double) {
        keepPlaying = true
        playbackThread = Thread {
            val sampleRate = 44100
            val bufferSize = maxOf(
                AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                ),
                4096
            )

            val track = buildAudioTrack(sampleRate, bufferSize) ?: return@Thread
            audioTrack = track
            track.play()

            val buffer       = ShortArray(bufferSize / 2)
            var phase        = 0.0
            val phaseInc     = 2.0 * PI * frequency / sampleRate
            var octavePhase  = 0.0
            val octaveInc    = 2.0 * PI * (frequency * 2.0) / sampleRate
            var amplitude    = 0.0

            while (keepPlaying && !Thread.currentThread().isInterrupted) {
                for (i in buffer.indices) {
                    if (amplitude < 0.35) amplitude += 0.000008
                    val s = amplitude * sin(phase) + (amplitude * 0.15) * sin(octavePhase)
                    buffer[i]    = (Short.MAX_VALUE * s).toInt().toShort()
                    phase        = (phase        + phaseInc)   % (2.0 * PI)
                    octavePhase  = (octavePhase  + octaveInc)  % (2.0 * PI)
                }
                if (track.state == AudioTrack.STATE_INITIALIZED) {
                    track.write(buffer, 0, buffer.size)
                } else {
                    break
                }
            }

            runCatching { track.stop() }
            runCatching { track.release() }
            audioTrack = null
        }.also {
            it.isDaemon = true
            it.start()
        }
    }

    private fun buildAudioTrack(sampleRate: Int, bufferSize: Int): AudioTrack? =
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize,
                    AudioTrack.MODE_STREAM
                )
            }
        }.getOrNull()

    private fun stopAudio() {
        keepPlaying = false
        playbackThread?.interrupt()
        playbackThread = null
        audioTrack?.let {
            runCatching { it.stop() }
            runCatching { it.release() }
        }
        audioTrack = null
    }

    private fun stopPlayback() {
        isPlaying         = false
        currentSongTitle  = ""
        currentMovieTitle = ""
        stopAudio()
        @Suppress("DEPRECATION")
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        stopAudio()
        isPlaying         = false
        currentSongTitle  = ""
        currentMovieTitle = ""
        super.onDestroy()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Ludwig Göransson Music Player"
                setSound(null, null)
            }
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun buildNotification(songTitle: String, movieTitle: String): Notification {
        val stopPendingIntent = PendingIntent.getService(
            this, 0,
            Intent(this, MusicService::class.java).apply { action = ACTION_STOP },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val openPendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentTitle(songTitle)
            .setContentText(movieTitle)
            .setContentIntent(openPendingIntent)
            .addAction(android.R.drawable.ic_media_pause, "Stop", stopPendingIntent)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    companion object {
        const val ACTION_PLAY        = "com.goransson.PLAY"
        const val ACTION_STOP        = "com.goransson.STOP"
        const val EXTRA_SONG_TITLE   = "song_title"
        const val EXTRA_MOVIE_TITLE  = "movie_title"
        const val EXTRA_FREQUENCY    = "frequency"
        const val CHANNEL_ID         = "goransson_music"
        const val NOTIFICATION_ID    = 42

        @Volatile var isPlaying         = false
        @Volatile var currentSongTitle  = ""
        @Volatile var currentMovieTitle = ""

        fun play(context: Context, song: Song, movie: Movie) {
            currentSongTitle  = song.title
            currentMovieTitle = movie.title
            isPlaying         = true
            val intent = Intent(context, MusicService::class.java).apply {
                action = ACTION_PLAY
                putExtra(EXTRA_SONG_TITLE,  song.title)
                putExtra(EXTRA_MOVIE_TITLE, movie.title)
                putExtra(EXTRA_FREQUENCY,   song.frequency)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            isPlaying         = false
            currentSongTitle  = ""
            currentMovieTitle = ""
            context.startService(Intent(context, MusicService::class.java).apply {
                action = ACTION_STOP
            })
        }
    }
}
