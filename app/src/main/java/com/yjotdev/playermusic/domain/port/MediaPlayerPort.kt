package com.yjotdev.playermusic.domain.port

import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions

interface MediaPlayerPort {
    fun play(track: MusicEntity, playlist: List<MusicEntity>, repeatMode: RepeatOptions)
    fun resume()
    fun pause()
    fun seekTo(position: Int)
    fun next()
    fun previous()
}