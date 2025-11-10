package com.yjotdev.playermusic.application.mvvm.model

import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions

data class PlayerMusicModel(
    val artistList: List<MusicListEntity> = listOf(),
    val playList: List<MusicListEntity> = listOf(),
    val selectedArtistList: MusicListEntity? = null,
    val selectedPlaylist: MusicListEntity? = null,
    val repeat: RepeatOptions = RepeatOptions.Current,
    val isRestartApp: Boolean = false,
    val isPlayList: Boolean = false
)