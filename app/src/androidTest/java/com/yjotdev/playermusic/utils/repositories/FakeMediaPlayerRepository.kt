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
    val playerState: StateFlow<PlayerEntity> = _playerState.asStateFlow()

    override fun play(track: MusicEntity, playlist: List<MusicEntity>, repeatMode: RepeatOptions) {
        this.currentPlaylist = playlist
        this.currentIndex = playlist.indexOf(track)

        _playerState.update {
            it.copy(
                isPlaying = true,
                currentTrack = track,
                totalDuration = track.musicDuration,
                currentPosition = 0,
                hasCompleted = false,
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

    override fun next() {
        if (currentPlaylist.isEmpty()) return
        // Asumimos un modo de repetición para la prueba, por ejemplo, 'All'.
        currentIndex = (currentIndex + 1) % currentPlaylist.size
        val nextTrack = currentPlaylist[currentIndex]
        // Llamamos a play con el modo de repetición por defecto para la prueba
        play(nextTrack, currentPlaylist, RepeatOptions.All)
    }

    override fun previous() {
        if (currentPlaylist.isEmpty()) return
        currentIndex = if (currentIndex - 1 < 0) currentPlaylist.size - 1 else currentIndex - 1
        val prevTrack = currentPlaylist[currentIndex]
        play(prevTrack, currentPlaylist, RepeatOptions.All)
    }

    /**
     * Simula que la canción actual ha terminado de reproducirse.
     * Esto es CRUCIAL para probar la lógica de `onTrackCompletion` en el ViewModel.
     */
    fun simulateTrackCompletion() {
        _playerState.update { it.copy(hasCompleted = true, isPlaying = false) }
    }

    /**
     * Simula el avance del tiempo en el reproductor.
     */
    fun simulateProgress(newPosition: Int) {
        _playerState.update { it.copy(currentPosition = newPosition) }
    }

    /**
     * Simula un error de reproducción.
     */
    fun injectError(errorMessage: String) {
        _playerState.update { it.copy(error = errorMessage, isPlaying = false) }
    }

    /**
     * Reinicia el estado del repositorio falso entre pruebas.
     * Es buena práctica llamarlo en una regla de @Before.
     */
    fun reset() {
        currentPlaylist = emptyList()
        currentIndex = -1
        _playerState.value = PlayerEntity()
    }
}
