package com.example.playermusic.ui.model

data class UiPlayerMusicModel(
    //Estados del reproductor de música
    val uiArtistList: List<MusicListModel> = listOf(),
    val uiMusicList: List<MusicModel> = listOf(),
    val uiPlayList: List<MusicListModel> = listOf(),
    val uiPlayListMusic: List<MusicModel> = listOf(),
    val uiIsPause: Boolean = true,
    val uiIsShuffle: Boolean = false,
    val uiIsRepeat: Boolean = false,
    val uiCountIndex: Int = 0,//Indice de lista de indices aleatorios
    val uiCurrentArtistIndex: Int = 0,//Indice actual del artista
    val uiCurrentPlayListIndex: Int = 0,//Indice actual de la playlist
    val uiCurrentMusicIndex: Int = 0,//Indice actual de la música por artista o playlist
    val uiCurrentDuration: Int = 0,//Duración actual de la música
    val uiPlayListName: String = "",
    val uiSelectedMusic: MusicModel = MusicModel(),
    val uiFilter: List<MusicListModel> = listOf()
)