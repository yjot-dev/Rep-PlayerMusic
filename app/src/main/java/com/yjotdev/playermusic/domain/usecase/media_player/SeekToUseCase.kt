package com.yjotdev.playermusic.domain.usecase.media_player

import javax.inject.Inject
import com.yjotdev.playermusic.domain.repository.MediaPlayerRepository

class SeekToUseCase @Inject constructor(
    private val mediaPlayerRepository: MediaPlayerRepository
) {
    operator fun invoke(position: Int) {
        mediaPlayerRepository.seekTo(position)
    }
}