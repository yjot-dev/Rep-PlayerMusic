package com.yjotdev.playermusic.domain.port

import kotlinx.coroutines.flow.StateFlow
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.PlayerEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions

interface MediaPlayerPort {
    // Flujo para que la UI observe el estado del reproductor
    val playerState: StateFlow<PlayerEntity>
    // Comandos que la UI puede enviar
    fun play(track: MusicEntity, playlist: List<MusicEntity>)
    fun resume()
    fun pause()
    fun seekTo(position: Int)
    fun stop()
    fun next(repeatMode: RepeatOptions)
    fun previous()
    fun onTrackCompletion(repeatMode: RepeatOptions)
}