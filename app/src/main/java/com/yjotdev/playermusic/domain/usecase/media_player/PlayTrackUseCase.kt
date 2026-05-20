package com.yjotdev.playermusic.domain.usecase.media_player

import javax.inject.Inject
import com.yjotdev.playermusic.domain.model.MusicModel
import com.yjotdev.playermusic.domain.utils.RepeatOptions
import com.yjotdev.playermusic.domain.repository.MediaPlayerRepository

class PlayTrackUseCase @Inject constructor(
    private val mediaPlayerRepository: MediaPlayerRepository
) {
    operator fun invoke(track: MusicModel, currentList: List<MusicModel>, repeat: RepeatOptions) {
        mediaPlayerRepository.play(track, currentList, repeat)
    }
}