package com.yjotdev.playermusic.domain.usecase.playlist

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.repository.PlaylistRepository

class GetPlaylistUseCase @Inject constructor(
    private val playListRepository: PlaylistRepository
) {
    operator fun invoke(): Flow<List<MusicListModel>> {
        return playListRepository.getPlayList()
    }
}