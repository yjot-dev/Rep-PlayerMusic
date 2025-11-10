package com.yjotdev.playermusic.domain.usecase.playlist

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.playermusic.domain.port.PlayListPort
import com.yjotdev.playermusic.domain.entity.MusicListEntity

@Singleton
class InsertPlayListUseCase @Inject constructor(
    private val playListPort: PlayListPort
) {
    suspend operator fun invoke(item: MusicListEntity){
        playListPort.insertPlayList(item)
    }
}