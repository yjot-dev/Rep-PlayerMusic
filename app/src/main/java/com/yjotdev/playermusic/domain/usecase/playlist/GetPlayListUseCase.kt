package com.yjotdev.playermusic.domain.usecase.playlist

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.PlayListPort

@Singleton
class GetPlayListUseCase @Inject constructor(
    private val playListPort: PlayListPort
) {
    operator fun invoke(): Flow<List<MusicListEntity>> {
        return playListPort.getPlayList()
    }
}