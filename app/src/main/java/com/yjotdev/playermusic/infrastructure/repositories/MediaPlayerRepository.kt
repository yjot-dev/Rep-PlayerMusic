package com.yjotdev.playermusic.infrastructure.repositories

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.domain.port.MediaPlayerPort
import com.yjotdev.playermusic.infrastructure.service.MusicService

@Singleton
class MediaPlayerRepository @Inject constructor(
    @ApplicationContext val context: Context
): MediaPlayerPort {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun play(track: MusicEntity, playlist: List<MusicEntity>, repeatMode: RepeatOptions) {
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