package com.yjotdev.playermusic.domain.usecase.media_player

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.port.MediaPlayerPort
import com.yjotdev.playermusic.domain.entity.RepeatOptions

@Singleton
class OnTrackCompletionUseCase @Inject constructor(
    private val mediaPlayerPort: MediaPlayerPort
) {
    operator fun invoke(repeatMode: RepeatOptions) {
        mediaPlayerPort.onTrackCompletion(repeatMode)
    }
}