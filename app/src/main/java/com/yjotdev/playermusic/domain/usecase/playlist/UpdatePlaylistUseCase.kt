package com.yjotdev.playermusic.domain.usecase.playlist

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.PlaylistPort

@Singleton
class UpdatePlaylistUseCase @Inject constructor(
    private val playListPort: PlaylistPort
) {
    suspend operator fun invoke(item: MusicListEntity){
        playListPort.updatePlayList(item)
    }
}