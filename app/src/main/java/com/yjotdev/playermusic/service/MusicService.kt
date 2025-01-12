package com.yjotdev.playermusic.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.yjotdev.playermusic.MainActivity
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.data.ObjectsManager
import com.yjotdev.playermusic.ui.viewModel.PlayerMusicViewModel

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var wakeLock: PowerManager.WakeLock
    private lateinit var vmPlayerMusic: PlayerMusicViewModel
    private val notificationId = 1
    private val channelId = "MUSIC_CHANNEL"

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                channelId,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
            val powerManager = getSystemService(POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MusicPlayerWakelock")
            wakeLock.acquire(5*60*1000L) // 5 minutos
            registerReceiver(stopServiceReceiver,IntentFilter("ACTION_STOP_SERVICE"),
                RECEIVER_NOT_EXPORTED)
            registerReceiver(bluetoothReceiver,IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED),
                RECEIVER_NOT_EXPORTED)
            registerReceiver(bluetoothReceiver,IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED),
                RECEIVER_NOT_EXPORTED)
        }else{
            registerReceiver(stopServiceReceiver,IntentFilter("ACTION_STOP_SERVICE"))
            registerReceiver(bluetoothReceiver,IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED))
            registerReceiver(bluetoothReceiver,IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED))
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = ObjectsManager.getMediaPlayer()
        vmPlayerMusic = ObjectsManager.getVmPlayerMusic()
        val musicName = intent?.getStringExtra("musicName") ?: ""
        val artistName = intent?.getStringExtra("artistName") ?: ""
        val notification = createNotification(musicName, artistName)
        startForeground(notificationId, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        //Libera memoria del wakelock
        wakeLock.release()
        //Libera memoria del mediaplayer
        ObjectsManager.releaseMediaPlayer()
        //Libera memoria del receiver
        unregisterReceiver(stopServiceReceiver)
        unregisterReceiver(bluetoothReceiver)
        //Detiene el estado de primer plano
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun createNotification(
        musicName: String,
        artistName: String
    ): Notification {
        val iconId = R.drawable.ic_launcher_foreground
        /* Ejecuta la app luego de dar click en la notificación
           Si la app esta cerrada la vuelve abrir */
        val openActivity = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java)
                .putExtra("IS_RESTART_APP", true),
            PendingIntent.FLAG_IMMUTABLE
        )
        // Detiene el servicio al dar click en el boton Detener
        val stopService = PendingIntent.getBroadcast(
            this,
            0,
            Intent("ACTION_STOP_SERVICE"),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(musicName.uppercase())
            .setContentText(artistName.uppercase())
            .setSmallIcon(iconId)
            .setContentIntent(openActivity)
            .addAction(iconId, "Detener", stopService)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private val stopServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            //Detiene el servicio
            stopSelf()
        }
    }

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                //Pausa reproductor de música si se ha desconectado el bluetooth
                BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                        vmPlayerMusic.setUiIsPause(true)
                    }
                }
                //Inicia reproductor de música si se ha conectado el bluetooth
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    if (!mediaPlayer.isPlaying) {
                        mediaPlayer.start()
                        vmPlayerMusic.setUiIsPause(false)
                    }
                }
            }
        }
    }
}