package com.yjotdev.playermusic.domain.usecase.media_player

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.domain.port.MediaPlayerPort

@Singleton
class PlayTrackUseCase @Inject constructor(
    private val mediaPlayerPort: MediaPlayerPort
) {
    operator fun invoke(track: MusicEntity, currentList: List<MusicEntity>, repeat: RepeatOptions) {
        mediaPlayerPort.play(track, currentList, repeat)
    }
}