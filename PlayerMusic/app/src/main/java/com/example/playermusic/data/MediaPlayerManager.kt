package com.example.playermusic.data

import android.media.MediaPlayer
import com.example.playermusic.ui.model.MusicModel

object MediaPlayerManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentMusic: MusicModel? = null

    fun getMediaPlayer(): MediaPlayer {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        return mediaPlayer!!
    }

    fun setCurrentMusic(musicName: String, artistName: String){
        currentMusic = MusicModel(
            musicName = musicName,
            artistName = artistName
        )
    }

    fun getCurrentMusic(): MusicModel {
        if(currentMusic == null){
            currentMusic = MusicModel()
        }
        return currentMusic!!
    }

    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        currentMusic = null
    }
}