package com.yjotdev.playermusic.domain.entity

data class PlayerEntity (
    val currentTrack: MusicEntity = MusicEntity(),
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val totalDuration: Int = 0,
    val error: String = "",
    val hasCompleted: Boolean = false
)