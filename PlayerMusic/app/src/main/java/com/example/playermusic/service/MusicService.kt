package com.example.playermusic.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.playermusic.R
import com.example.playermusic.data.MediaPlayerManager
import com.example.playermusic.ui.model.MusicModel

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var currentMusic: MusicModel
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "MUSIC_CHANNEL",
                "Music Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
            val powerManager = getSystemService(POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MusicPlayerWakelock")
            wakeLock.acquire(5*60*1000L /*5 minutos*/)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayerManager.getMediaPlayer()
        currentMusic = MediaPlayerManager.getCurrentMusic()
        val notification = createNotification()
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        //Libera memoria del wakelock
        wakeLock.release()
        //Detiene el estado de primer plano
        stopForeground(STOP_FOREGROUND_REMOVE)
        //Detiene el servicio
        stopSelf()
    }

    private fun createNotification(): Notification {
        val iconId = R.drawable.ic_launcher_foreground
        return NotificationCompat.Builder(this, "MUSIC_CHANNEL")
            .setContentTitle(currentMusic.artistName)
            .setContentText(currentMusic.musicName)
            .setSmallIcon(iconId)
            .build()
    }
}