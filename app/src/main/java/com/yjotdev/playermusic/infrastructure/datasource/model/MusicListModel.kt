package com.yjotdev.playermusic.infrastructure.datasource.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yjotdev.playermusic.domain.entity.MusicEntity

@Entity(tableName = "playlist")
data class MusicListModel(
    //Datos de la PlayList
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val musicList: List<MusicEntity> = listOf(),
    val totalArtistMusic: String = "",
    val totalArtistAlbum: String = ""
)