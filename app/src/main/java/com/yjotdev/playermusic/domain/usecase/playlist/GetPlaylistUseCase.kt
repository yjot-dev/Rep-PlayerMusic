package com.yjotdev.playermusic.domain.usecase.playlist

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.PlaylistPort

@Singleton
class GetPlaylistUseCase @Inject constructor(
    private val playListPort: PlaylistPort
) {
    operator fun invoke(): Flow<List<MusicListEntity>> {
        return playListPort.getPlayList()
    }
}