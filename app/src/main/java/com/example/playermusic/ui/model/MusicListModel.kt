package com.example.playermusic.ui.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class MusicListModel(
    //Datos de la PlayList
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val musicList: List<MusicModel> = listOf(),
    val totalArtistMusic: String = totalArtistMusic(musicList),
    val totalArtistAlbum: String = totalArtistAlbum(musicList)
)

private fun totalArtistMusic(musicList: List<MusicModel>) : String{
    return when(val res = musicList.count()){
        1 -> "$res canción"
        else -> "$res canciones"
    }
}

private fun totalArtistAlbum(musicList: List<MusicModel>) : String{
    return when(val res = musicList.distinctBy{ it.albumName }.count()){
        1 -> "$res álbum"
        else -> "$res álbumes"
    }
}