package com.yjotdev.playermusic.domain.usecase.media_player

import javax.inject.Inject
import com.yjotdev.playermusic.domain.repository.MediaPlayerRepository

class PauseTrackUseCase @Inject constructor(
    private val mediaPlayerRepository: MediaPlayerRepository
) {
    operator fun invoke() {
        mediaPlayerRepository.pause()
    }
}