package com.yjotdev.playermusic.domain.port

import com.yjotdev.playermusic.domain.entity.MusicListEntity

interface ArtistListPort {
    suspend fun getArtistMusicList(): List<MusicListEntity>
}