package com.yjotdev.playermusic.data.local.mapper

import com.yjotdev.playermusic.data.local.entity.MusicListEntity
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.model.MusicModel

fun MusicListEntity.toDomain() = MusicListModel(
    id = this.id,
    name = this.name,
    musicList = this.musicList,
    totalArtistMusic = totalArtistMusic(this.musicList),
    totalArtistAlbum = totalArtistAlbum(this.musicList)
)

fun MusicListModel.toBD() = MusicListEntity(
    id = this.id,
    name = this.name,
    musicList = this.musicList
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