package com.yjotdev.playermusic.utils.repositories

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.yjotdev.playermusic.domain.repository.PlayerStateRepository
import com.yjotdev.playermusic.domain.model.PlayerModel

@Singleton
class FakePlayerStateRepositoryImpl @Inject constructor()
    : PlayerStateRepository {
    private val _playerState = MutableStateFlow(PlayerModel())
    override val playerState = _playerState.asStateFlow()
}