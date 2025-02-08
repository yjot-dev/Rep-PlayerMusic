package com.yjotdev.playermusic.service

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
import androidx.core.app.NotificationCompat
import com.yjotdev.playermusic.MainActivity
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.data.ObjectsManager
import com.yjotdev.playermusic.ui.viewModel.DatabaseProcess
import com.yjotdev.playermusic.ui.viewModel.PlayerMusicViewModel

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var wakeLock: PowerManager.WakeLock
    private lateinit var vmPlayerMusic: PlayerMusicViewModel
    private lateinit var dbProcess: DatabaseProcess
    private val notificationId = 1
    private val channelId = "MUSIC_CHANNEL"
    private val actionStop = "ACTION_STOP_SERVICE"

    @Suppress("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        //Inicializar MediaPlayer, Viewmodel y DatabaseProcess
        mediaPlayer = ObjectsManager.getMediaPlayer()
        vmPlayerMusic = ObjectsManager.getVmPlayerMusic()
        dbProcess = DatabaseProcess()
        //Crea el wakelock para mantener la pantalla encendida
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
            "MyApp::MusicPlayerWakelock")
        wakeLock.acquire(5*60*1000L) // 5 minutos
        //Filtros para el receiver
        val filter1 = IntentFilter(actionStop)
        val filter2 = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        }
        //Registra receiver
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){//SDK >= 33
            channelNotification()
            registerReceiver(stopServiceReceiver, filter1, RECEIVER_NOT_EXPORTED)
            registerReceiver(bluetoothReceiver, filter2, RECEIVER_NOT_EXPORTED)
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//SDK >= 26
            channelNotification()
            registerReceiver(stopServiceReceiver, filter1)
            registerReceiver(bluetoothReceiver, filter2)
        }else{
            registerReceiver(stopServiceReceiver, filter1)
            registerReceiver(bluetoothReceiver, filter2)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val isSave = intent?.getBooleanExtra("isSaved", false) ?: false
        if(isSave){
            //Guardo las configuraciones del usuario
            val uiState = vmPlayerMusic.uiState.value
            dbProcess.saveConfig(applicationContext, valueRepeat = uiState.uiRepeat)
            dbProcess.saveConfig(applicationContext, isShuffle = uiState.uiIsShuffle)
            dbProcess.saveConfig(applicationContext, isPlayList = uiState.uiIsPlayList)
            dbProcess.saveConfig(applicationContext, index0 = uiState.uiCurrentArtistListIndex)
            dbProcess.saveConfig(applicationContext, index1 = uiState.uiCurrentPlayListIndex)
            dbProcess.saveConfig(applicationContext, index2 = uiState.uiCurrentMusicListIndex)
        }else{
            //Obtiene la musica actual
            val musicName = intent?.getStringExtra("musicName") ?: ""
            val artistName = intent?.getStringExtra("artistName") ?: ""
            //Notificacion
            val notification = createNotification(musicName, artistName)
            startForeground(notificationId, notification)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        //Detiene el estado de primer plano
        stopForeground(STOP_FOREGROUND_REMOVE)
        //Libera memoria del receiver
        unregisterReceiver(stopServiceReceiver)
        unregisterReceiver(bluetoothReceiver)
        //Libera memoria del wakelock
        wakeLock.release()
        //Libera memoria del mediaplayer
        ObjectsManager.releaseMediaPlayer()
    }

    private fun channelNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//SDK >= 26
            val serviceChannel = NotificationChannel(
                channelId,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(
        musicName: String,
        artistName: String
    ): Notification {
        val iconId = R.mipmap.ic_launcher
        /* Ejecuta la app luego de dar click en la notificación
           Si la app esta cerrada la vuelve abrir */
        val openActivity = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java)
                .putExtra("IS_RESTART_APP", true),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_IMMUTABLE
            else 0
        )
        // Detiene el servicio al dar click en el boton Detener
        val stopService = PendingIntent.getBroadcast(
            this,
            0,
            Intent(actionStop),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_IMMUTABLE
            else 0
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