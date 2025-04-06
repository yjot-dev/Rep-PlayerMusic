package com.yjotdev.playermusic.domain.usecase

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.playermusic.domain.port.PlayListRepository
import com.yjotdev.playermusic.domain.entity.MusicListEntity

@Singleton
class InsertPlayListUseCase @Inject constructor(
    private val playListRepository: PlayListRepository
) {
    suspend operator fun invoke(item: MusicListEntity){
        playListRepository.insertPlayList(item)
    }
}