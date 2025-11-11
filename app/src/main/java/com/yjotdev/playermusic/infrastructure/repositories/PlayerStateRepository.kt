package com.yjotdev.playermusic.infrastructure.repositories

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.yjotdev.playermusic.domain.entity.PlayerEntity
import com.yjotdev.playermusic.domain.port.PlayerStatePort

@Singleton
class PlayerStateRepository @Inject constructor()
    : PlayerStatePort {
    private val _playerState = MutableStateFlow(PlayerEntity())
    override val playerState = _playerState.asStateFlow()

    fun updateState(newState: PlayerEntity) {
        _playerState.value = newState
    }
}