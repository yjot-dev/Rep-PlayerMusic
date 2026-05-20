package com.yjotdev.playermusic.domain.repository

import com.yjotdev.playermusic.domain.model.MusicModel
import com.yjotdev.playermusic.domain.utils.RepeatOptions

interface MediaPlayerRepository {
    fun play(track: MusicModel, playlist: List<MusicModel>, repeatMode: RepeatOptions)
    fun resume()
    fun pause()
    fun seekTo(position: Int)
    fun next()
    fun previous()
}