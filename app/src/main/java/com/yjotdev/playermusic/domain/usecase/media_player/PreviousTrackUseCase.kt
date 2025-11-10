package com.yjotdev.playermusic.domain.usecase.media_player

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.port.MediaPlayerPort

@Singleton
class PreviousTrackUseCase @Inject constructor(
    private val mediaPlayerPort: MediaPlayerPort
) {
    operator fun invoke() {
        mediaPlayerPort.previous()
    }
}