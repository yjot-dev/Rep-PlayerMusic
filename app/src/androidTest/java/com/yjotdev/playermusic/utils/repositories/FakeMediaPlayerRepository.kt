package com.yjotdev.playermusic.utils.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.PlayerEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.domain.port.MediaPlayerPort

@Singleton
class FakeMediaPlayerRepository @Inject constructor(): MediaPlayerPort {

    private var currentPlaylist: List<MusicEntity> = emptyList()
    private var currentIndex = -1

    private val _playerState = MutableStateFlow(PlayerEntity())
    override val playerState: StateFlow<PlayerEntity> = _playerState.asStateFlow()

    // --- Implementación de los métodos del puerto ---

    override fun play(track: MusicEntity, playlist: List<MusicEntity>) {
        this.currentPlaylist = playlist
        this.currentIndex = playlist.indexOf(track)

        _playerState.update {
            it.copy(
                isPlaying = true,
                currentTrack = track,
                totalDuration = track.musicDuration, // Usamos la duración del track
                currentPosition = 0,
                error = ""
            )
        }
    }

    override fun resume() {
        if (_playerState.value.currentTrack != MusicEntity()) {
            _playerState.update { it.copy(isPlaying = true) }
        }
    }

    override fun pause() {
        _playerState.update { it.copy(isPlaying = false) }
    }

    override fun seekTo(position: Int) {
        _playerState.update { it.copy(currentPosition = position) }
    }

    override fun stop() {
        _playerState.value = PlayerEntity() // Resetea al estado inicial
    }

    override fun next(repeatMode: RepeatOptions) {
        if (currentPlaylist.isEmpty()) return
        currentIndex = newIndex(repeatMode)
        val nextTrack = currentPlaylist[currentIndex]
        play(nextTrack, currentPlaylist)
    }

    override fun previous() {
        if (currentPlaylist.isEmpty()) return
        currentIndex = if (currentIndex - 1 < 0) currentPlaylist.size - 1 else currentIndex - 1
        val prevTrack = currentPlaylist[currentIndex]
        play(prevTrack, currentPlaylist)
    }

    override fun onTrackCompletion(repeatMode: RepeatOptions) {
        currentIndex = newIndex(repeatMode)
        val nextTrack = currentPlaylist[currentIndex]
        play(nextTrack, currentPlaylist)
    }

    // --- Métodos de Control para Pruebas ---
    // Estos métodos NO están en el puerto, pero nos permiten manipular el estado desde nuestras pruebas.

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

    /**
     * Simula un error de reproducción.
     */
    fun injectError(errorMessage: String) {
        _playerState.update { it.copy(error = errorMessage, isPlaying = false) }
    }

    /**
     * Simula el avance del tiempo en el reproductor.
     */
    fun simulateProgress(newPosition: Int) {
        _playerState.update { it.copy(currentPosition = newPosition) }
    }
}
