package com.yjotdev.playermusic.application.mvvm.model

data class PlayerMusicModel(
    //Estados del reproductor de música
    val uiArtistList: List<MusicListModel> = listOf(),
    val uiPlayList: List<MusicListModel> = listOf(),
    val uiMusicList: List<MusicModel> = listOf(),
    val uiRepeat: RepeatOptions = RepeatOptions.Off,
    val uiIsShuffle: Boolean = false,
    val uiIsCompletion: Boolean = false,
    val uiIsPlayList: Boolean = false,
    val uiIsRestartApp: Boolean = false,
    val uiCountIndex: Int = 0,//Indice de lista de indices aleatorios
    val uiCurrentArtistListIndex: Int = 0,//Indice actual del artista
    val uiCurrentPlayListIndex: Int = 0,//Indice actual de la playlist
    val uiCurrentMusicListIndex: Int = 0,//Indice actual de la música
    val uiCurrentDuration: Int = 0,//Duración actual de la música
)