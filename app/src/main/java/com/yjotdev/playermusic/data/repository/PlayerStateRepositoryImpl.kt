package com.yjotdev.playermusic.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.yjotdev.playermusic.domain.model.PlayerModel
import com.yjotdev.playermusic.domain.repository.PlayerStateRepository

@Singleton
class PlayerStateRepositoryImpl @Inject constructor()
    : PlayerStateRepository {
    private val _playerState = MutableStateFlow(PlayerModel())
    override val playerState = _playerState.asStateFlow()

    fun updateState(newState: PlayerModel) {
        _playerState.value = newState
    }
}