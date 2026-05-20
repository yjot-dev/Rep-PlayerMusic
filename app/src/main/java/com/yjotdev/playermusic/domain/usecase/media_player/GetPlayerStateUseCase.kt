package com.yjotdev.playermusic.domain.usecase.media_player

import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import com.yjotdev.playermusic.domain.model.PlayerModel
import com.yjotdev.playermusic.domain.repository.PlayerStateRepository

class GetPlayerStateUseCase @Inject constructor(
    private val playerStateRepository: PlayerStateRepository
) {
    operator fun invoke(): StateFlow<PlayerModel> {
        return playerStateRepository.playerState
    }
}
