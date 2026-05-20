package com.yjotdev.playermusic.domain.model

data class PlayerModel (
    val currentTrack: MusicModel = MusicModel(),
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val totalDuration: Int = 0,
    val error: String = "",
    val hasCompleted: Boolean = false
)