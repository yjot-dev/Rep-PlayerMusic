package com.yjotdev.playermusic.utils.repositories

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.yjotdev.playermusic.domain.port.PlayerStatePort
import com.yjotdev.playermusic.domain.entity.PlayerEntity

@Singleton
class FakePlayerStateRepository @Inject constructor()
    : PlayerStatePort {
    private val _playerState = MutableStateFlow(PlayerEntity())
    override val playerState = _playerState.asStateFlow()
}