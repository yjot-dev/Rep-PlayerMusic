package com.yjotdev.playermusic.domain.repository

import com.yjotdev.playermusic.domain.model.MusicListModel

interface ArtistListRepository {
    suspend fun getArtistMusicList(): List<MusicListModel>
}