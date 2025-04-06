package com.yjotdev.playermusic.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.PlayListRepository

@Singleton
class UpdatePlayListUseCase @Inject constructor(
    private val playListRepository: PlayListRepository
) {
    suspend operator fun invoke(item: MusicListEntity){
        playListRepository.updatePlayList(item)
    }
}