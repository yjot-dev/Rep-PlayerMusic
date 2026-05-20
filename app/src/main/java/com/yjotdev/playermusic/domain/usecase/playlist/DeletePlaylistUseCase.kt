package com.yjotdev.playermusic.domain.usecase.playlist

import javax.inject.Inject
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.repository.PlaylistRepository

class DeletePlaylistUseCase @Inject constructor(
    private val playListRepository: PlaylistRepository
) {
    suspend operator fun invoke(item: MusicListModel){
        playListRepository.deletePlayList(item)
    }
}