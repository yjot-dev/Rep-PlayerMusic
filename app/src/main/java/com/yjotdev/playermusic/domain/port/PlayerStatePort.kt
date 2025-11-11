package com.yjotdev.playermusic.domain.port

import com.yjotdev.playermusic.domain.entity.PlayerEntity
import kotlinx.coroutines.flow.StateFlow

interface PlayerStatePort {
    val playerState: StateFlow<PlayerEntity>
}