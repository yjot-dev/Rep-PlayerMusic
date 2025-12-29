package com.yjotdev.playermusic.utils.repositories

import kotlinx.coroutines.flow.MutableStateFlow
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
}
