package com.yjotdev.playermusic.domain.utils

import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private val _uiIsPause by lazy { MutableStateFlow(true) }
    val uiIsPause = _uiIsPause.asStateFlow()

    /** Obtiene el reproductor de música **/
    fun getMediaPlayer(): MediaPlayer {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        return mediaPlayer!!
    }
    /** Actualiza estado de pausa **/
    fun setUiIsPause(value: Boolean){
        _uiIsPause.update { value }
    }
    /** Libera los recursos del app luego de finalizar el servicio **/
    fun releaseMediaPlayer() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        _uiIsPause.value = true
    }
}