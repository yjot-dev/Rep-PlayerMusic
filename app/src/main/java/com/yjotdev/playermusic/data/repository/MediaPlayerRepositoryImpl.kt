package com.yjotdev.playermusic.data.repository

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.model.MusicModel
import com.yjotdev.playermusic.domain.utils.RepeatOptions
import com.yjotdev.playermusic.domain.repository.MediaPlayerRepository
import com.yjotdev.playermusic.data.service.MusicService

@Singleton
class MediaPlayerRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context
): MediaPlayerRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun play(track: MusicModel, playlist: List<MusicModel>, repeatMode: RepeatOptions) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "PLAY"
            putExtra("TRACK", track)
            putParcelableArrayListExtra("PLAYLIST", ArrayList(playlist))
            putExtra("REPEAT_MODE", repeatMode)
        }
        context.startForegroundService(intent)
    }

    override fun resume() {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "RESUME"
        }
        context.startService(intent)
    }

    override fun pause() {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "PAUSE"
        }
        context.startService(intent)
    }

    override fun seekTo(position: Int) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "SEEK_TO"
            putExtra("POSITION", position)
        }
        context.startService(intent)
    }

    override fun next() {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "NEXT"
        }
        context.startService(intent)
    }

    override fun previous() {
        val intent = Intent(context, MusicService::class.java).apply {
            action = "PREVIOUS"
        }
        context.startService(intent)
    }
}