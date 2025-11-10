package com.yjotdev.playermusic.domain.entity

data class MusicListEntity(
    val id: Int = 0,
    val name: String = "",
    val musicList: List<MusicEntity> = listOf(),
    val totalArtistMusic: String = "",
    val totalArtistAlbum: String = ""
)