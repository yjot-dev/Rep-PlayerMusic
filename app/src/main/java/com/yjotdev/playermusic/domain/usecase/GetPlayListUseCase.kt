package com.yjotdev.playermusic.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.PlayListRepository

@Singleton
class GetPlayListUseCase @Inject constructor(
    private val playListRepository: PlayListRepository
) {
    operator fun invoke(): Flow<List<MusicListEntity>> {
        return playListRepository.getPlayList()
    }
}