package com.yjotdev.playermusic.domain.repository

import com.yjotdev.playermusic.domain.model.PlayerModel
import kotlinx.coroutines.flow.StateFlow

interface PlayerStateRepository {
    val playerState: StateFlow<PlayerModel>
}