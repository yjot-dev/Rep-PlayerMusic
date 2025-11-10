package com.yjotdev.playermusic.domain.usecase.media_player

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow
import com.yjotdev.playermusic.domain.entity.PlayerEntity
import com.yjotdev.playermusic.domain.port.MediaPlayerPort

@Singleton
class GetPlayerStateUseCase @Inject constructor(
    private val mediaPlayerPort: MediaPlayerPort
) {
    operator fun invoke(): StateFlow<PlayerEntity> {
        return mediaPlayerPort.playerState
    }
}
