package com.yjotdev.playermusic.domain.model

data class MusicListModel(
    val id: Int = 0,
    val name: String = "",
    val musicList: List<MusicModel> = listOf(),
    val totalArtistMusic: String = "",
    val totalArtistAlbum: String = ""
)