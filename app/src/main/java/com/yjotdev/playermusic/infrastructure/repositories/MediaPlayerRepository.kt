package com.yjotdev.playermusic.infrastructure.repositories

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.PlayerEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.domain.port.MediaPlayerPort
import com.yjotdev.playermusic.infrastructure.service.MusicService

@Singleton
class MediaPlayerRepository @Inject constructor(
    @ApplicationContext val context: Context
): MediaPlayerPort, MediaPlayer.OnCompletionListener {
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null
    private var currentPlaylist: List<MusicEntity> = emptyList()
    private var currentIndex: Int = -1

    // --- Implementación del Flujo de Estado ---
    private val _playerState = MutableStateFlow(PlayerEntity())
    override val playerState: StateFlow<PlayerEntity> = _playerState.asStateFlow()

    init {
        // Inicializa el MediaPlayer una sola vez
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnCompletionListener(this)
    }

    // --- Implementación de los Comandos del Puerto ---
    @RequiresApi(Build.VERSION_CODES.O)
    override fun play(track: MusicEntity, playlist: List<MusicEntity>) {
        this.currentPlaylist = playlist
        this.currentIndex = playlist.indexOf(track)
        playTrackAtIndex(this.currentIndex)
    }

    override fun resume() {
        mediaPlayer?.takeIf { !it.isPlaying }?.apply {
            start()
            _playerState.value = _playerState.value.copy(isPlaying = true)
            startProgressUpdates()
        }
    }

    override fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.apply {
            pause()
            _playerState.value = _playerState.value.copy(isPlaying = false)
            stopProgressUpdates()
        }
    }

    override fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
        _playerState.value = _playerState.value.copy(currentPosition = position)
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        _playerState.value = PlayerEntity()
        stopProgressUpdates()
        stopMusicService()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun next(repeatMode: RepeatOptions) {
        if (currentPlaylist.isEmpty()) return
        playTrackAtIndex(newIndex(repeatMode))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun previous() {
        if (currentPlaylist.isEmpty()) return
        val prevIndex = if (currentIndex - 1 < 0) {
            currentPlaylist.size - 1 // Vuelve al final si está en la primera
        } else {
            currentIndex - 1
        }
        playTrackAtIndex(prevIndex)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onTrackCompletion(repeatMode: RepeatOptions) {
        _playerState.value = _playerState.value.copy(hasCompleted = false)
        playTrackAtIndex(newIndex(repeatMode))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCompletion(mp: MediaPlayer?) {
        _playerState.value = _playerState.value.copy(hasCompleted = true)
        stopProgressUpdates()
    }

    private fun newIndex(repeatMode: RepeatOptions): Int{
        return when (repeatMode) {
            RepeatOptions.Current -> {
                // Repetir la pista actual. Simplemente nos quedamos en el mismo índice.
                currentIndex
            }
            RepeatOptions.Shuffle -> {
                // Elegir una pista aleatoria que no sea la actual.
                if (currentPlaylist.size > 1) {
                    var randomIndex = (0 until currentPlaylist.size).random()
                    while (randomIndex == currentIndex) {
                        randomIndex = (0 until currentPlaylist.size).random()
                    }
                    randomIndex
                } else {
                    currentIndex // Si solo hay una, se repite.
                }
            }
            RepeatOptions.All -> {
                // Pasar a la siguiente de forma secuencial.
                (currentIndex + 1) % currentPlaylist.size
            }
        }
    }

    // --- Gestión del indice de la lista de reproducción ---
    @RequiresApi(Build.VERSION_CODES.O)
    private fun playTrackAtIndex(index: Int) {
        if (index < 0 || index >= currentPlaylist.size) {
            stop() // Índice inválido, paramos la reproducción.
            return
        }
        val track = currentPlaylist[index]
        this.currentIndex = index // Actualizamos el índice
        mediaPlayer?.apply {
            try {
                reset()
                setDataSource(context, track.musicPath.toUri())
                prepare()
                start()

                _playerState.value = PlayerEntity(
                    currentTrack = track,
                    isPlaying = true,
                    totalDuration = duration,
                    currentPosition = 0
                )
                startProgressUpdates()
                startMusicService(track)
            } catch (_: Exception) {
                _playerState.value = PlayerEntity(error = "No se pudo reproducir el archivo.")
                stop()
            }
        }
    }

    // --- Gestión del Servicio y Progreso ---
    private fun startProgressUpdates() {
        stopProgressUpdates()
        progressJob = scope.launch {
            while (_playerState.value.isPlaying) {
                _playerState.value = _playerState.value.copy(
                    currentPosition = mediaPlayer?.currentPosition ?: 0
                )
                delay(1000)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMusicService(track: MusicEntity) {
        val intent = Intent(context, MusicService::class.java).apply {
            // Aquí pasarías información de la canción para la notificación
            putExtra("TRACK_NAME", track.musicName)
            putExtra("ARTIST_NAME", track.artistName)
        }
        context.startForegroundService(intent)
    }

    private fun stopMusicService() {
        val intent = Intent(context, MusicService::class.java)
        context.stopService(intent)
    }

    /**
     * Libera los recursos nativos del MediaPlayer.
     * Este proceso es crucial para prevenir fugas de recursos.
     * Aunque no se llame en el flujo de navegación normal (ya que el adaptador es un Singleton
     * para permitir la reproducción de fondo), se mantiene como un mecanismo de limpieza explícito
     * si fuera necesario en el futuro o en hooks del ciclo de vida de la aplicación.
     */
    @Suppress("unused")
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        stopMusicService()
    }
}