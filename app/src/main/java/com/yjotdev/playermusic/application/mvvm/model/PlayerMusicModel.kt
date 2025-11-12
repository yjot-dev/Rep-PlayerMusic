package com.yjotdev.playermusic.application.mvvm.model

import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions

data class PlayerMusicModel(
    //Estados de la UI
    val artistList: List<MusicListEntity> = listOf(),
    val playList: List<MusicListEntity> = listOf(),
    val selectedArtistList: MusicListEntity? = null,
    val selectedPlaylist: MusicListEntity? = null,
    val repeat: RepeatOptions = RepeatOptions.Current,
    //Estados auxiliares
    val isRestartApp: Boolean = false,
    val isPlayList: Boolean = false,
    val itemSelected: List<MusicEntity> = listOf()
)