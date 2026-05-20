package com.yjotdev.playermusic.domain.usecase.artist_list

import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.repository.ArtistListRepository
import javax.inject.Inject

class GetArtistListUseCase @Inject constructor(
    private val artistListRepository: ArtistListRepository
) {
    suspend operator fun invoke(): List<MusicListModel> {
        return artistListRepository.getArtistMusicList()
    }
}