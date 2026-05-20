package com.yjotdev.playermusic.domain.usecase.playlist

import javax.inject.Inject
import com.yjotdev.playermusic.domain.repository.PlaylistRepository
import com.yjotdev.playermusic.domain.model.MusicListModel

class InsertPlaylistUseCase @Inject constructor(
    private val playListRepository: PlaylistRepository
) {
    suspend operator fun invoke(item: MusicListModel){
        playListRepository.insertPlayList(item)
    }
}