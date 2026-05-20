package com.yjotdev.playermusic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yjotdev.playermusic.domain.model.MusicModel

@Entity(tableName = "playlist")
data class MusicListEntity(
    //Datos de la PlayList
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val musicList: List<MusicModel> = listOf(),
    val totalArtistMusic: String = "",
    val totalArtistAlbum: String = ""
)