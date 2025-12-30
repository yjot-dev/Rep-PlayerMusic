package com.yjotdev.playermusic.domain.usecase.playlist

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.playermusic.domain.port.PlaylistPort
import com.yjotdev.playermusic.domain.entity.MusicListEntity

@Singleton
class InsertPlaylistUseCase @Inject constructor(
    private val playListPort: PlaylistPort
) {
    suspend operator fun invoke(item: MusicListEntity){
        playListPort.insertPlayList(item)
    }
}