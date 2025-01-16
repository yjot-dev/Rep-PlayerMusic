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
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
        mediaPlayer?.stop()
=======
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
        mediaPlayer?.release()
        mediaPlayer = null
        vmPlayerMusic = null
        database?.close()
        database = null
<<<<<<< HEAD
=======
=======
>>>>>>> master
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        database?.close()
        database = null
        vmPlayerMusic = null
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
=======
>>>>>>> master
    }
}