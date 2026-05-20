package com.yjotdev.playermusic.domain.utils

import com.yjotdev.playermusic.domain.model.MusicModel

/**
 * Contiene funciones de utilidad relacionadas con los casos de uso.
 * Pertenece a la capa de Domain.
 */
object Helper {
    /** Obtiene el total de canciones del artista **/
    fun getTotalArtistMusic(musicList: List<MusicModel>): String{
        return when(val res = musicList.count()){
            1 -> "$res canción"
            else -> "$res canciones"
        }
    }

    /** Obtiene el total de albumes del artista **/
    fun getTotalArtistAlbum(musicList: List<MusicModel>): String{
        return when(val res = musicList.distinctBy{ it.albumName }.count()){
            1 -> "$res álbum"
            else -> "$res álbumes"
        }
    }
}