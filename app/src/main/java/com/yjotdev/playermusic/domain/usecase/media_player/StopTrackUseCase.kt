package com.yjotdev.playermusic.domain.usecase.media_player

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.playermusic.domain.port.MediaPlayerPort

@Singleton
class StopTrackUseCase @Inject constructor(
    private val mediaPlayerPort: MediaPlayerPort
){
    operator fun invoke() {
        mediaPlayerPort.stop()
    }
}