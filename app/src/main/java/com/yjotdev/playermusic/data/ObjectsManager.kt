package com.yjotdev.playermusic.data

import android.media.MediaPlayer
import com.yjotdev.playermusic.MainApplication
import com.yjotdev.playermusic.ui.viewModel.PlayerMusicViewModel

object ObjectsManager {
    private var mediaPlayer: MediaPlayer? = null
    private var vmPlayerMusic: PlayerMusicViewModel? = null
    private var database: PlayListDatabase? = null

    fun getMediaPlayer(): MediaPlayer {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        return mediaPlayer!!
    }

    fun getVmPlayerMusic(): PlayerMusicViewModel {
        if(vmPlayerMusic == null){
            vmPlayerMusic = PlayerMusicViewModel()
        }
        return vmPlayerMusic!!
    }

    fun getBD(): PlayListDatabase {
        if(database == null){
            database = MainApplication.database
        }
        return database!!
    }
    /** Libera los recursos del app luego de finalizar el servicio **/
    fun releaseMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        vmPlayerMusic = null
        database?.close()
        database = null
    }
}