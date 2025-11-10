package com.yjotdev.playermusic.domain.usecase.artist_list

import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.ArtistListPort
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetArtistListUseCase @Inject constructor(
    private val artistListPort: ArtistListPort
) {
    suspend operator fun invoke(): List<MusicListEntity> {
        return artistListPort.getArtistMusicList()
    }
}