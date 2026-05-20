package com.yjotdev.playermusic

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import com.yjotdev.playermusic.domain.model.MusicModel
import com.yjotdev.playermusic.domain.utils.RepeatOptions
import com.yjotdev.playermusic.domain.repository.MediaPlayerRepository
import com.yjotdev.playermusic.domain.usecase.media_player.NextTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PauseTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PlayTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PreviousTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.ResumeTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.SeekToUseCase

/**
 * Pruebas unitarias para los casos de uso que controlan el reproductor multimedia.
 */
class MediaPlayerUseCaseTest {

    private lateinit var mediaPlayerRepository: MediaPlayerRepository
    private lateinit var playTrackUseCase: PlayTrackUseCase
    private lateinit var pauseTrackUseCase: PauseTrackUseCase
    private lateinit var resumeTrackUseCase: ResumeTrackUseCase
    private lateinit var nextTrackUseCase: NextTrackUseCase
    private lateinit var previousTrackUseCase: PreviousTrackUseCase
    private lateinit var seekToUseCase: SeekToUseCase

    @Before
    fun setUp() {
        mediaPlayerRepository = mockk(relaxed = true)
        playTrackUseCase = PlayTrackUseCase(mediaPlayerRepository)
        pauseTrackUseCase = PauseTrackUseCase(mediaPlayerRepository)
        resumeTrackUseCase = ResumeTrackUseCase(mediaPlayerRepository)
        nextTrackUseCase = NextTrackUseCase(mediaPlayerRepository)
        previousTrackUseCase = PreviousTrackUseCase(mediaPlayerRepository)
        seekToUseCase = SeekToUseCase(mediaPlayerRepository)
    }

    @Test
    fun whenPlayTrackUseCaseIsInvokedThenPortPlayMethodIsCalled() {
        // Given
        val track = MusicModel(
            musicPath = "path/to/track",
            musicDuration = 200,
            musicName = "Track Name",
        )
        val playlist = listOf(track)
        val repeatMode = RepeatOptions.All

        // When
        playTrackUseCase(track, playlist, repeatMode)

        // Then
        verify(exactly = 1) { mediaPlayerRepository.play(track, playlist, repeatMode) }
    }

    @Test
    fun whenPauseTrackUseCaseIsInvokedThenPortPauseMethodIsCalled() {
        // When
        pauseTrackUseCase()

        // Then
        verify(exactly = 1) { mediaPlayerRepository.pause() }
    }

    @Test
    fun whenResumeTrackUseCaseIsInvokedThenPortResumeMethodIsCalled() {
        // When
        resumeTrackUseCase()

        // Then
        verify(exactly = 1) { mediaPlayerRepository.resume() }
    }

    @Test
    fun whenNextTrackUseCaseIsInvokedThenPortNextMethodIsCalled() {
        // When
        nextTrackUseCase()

        // Then
        verify(exactly = 1) { mediaPlayerRepository.next() }
    }

    @Test
    fun whenPreviousTrackUseCaseIsInvokedThenPortPreviousMethodIsCalled() {
        // When
        previousTrackUseCase()

        // Then
        verify(exactly = 1) { mediaPlayerRepository.previous() }
    }

    @Test
    fun whenSeekToUseCaseIsInvokedThenPortSeekToMethodIsCalled() {
        // Given
        val position = 30000 // 30 segundos

        // When
        seekToUseCase(position)

        // Then
        verify(exactly = 1) { mediaPlayerRepository.seekTo(position) }
    }
}