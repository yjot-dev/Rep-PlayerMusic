package com.yjotdev.playermusic.service

<<<<<<< HEAD
=======
import android.annotation.SuppressLint
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
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
<<<<<<< HEAD
=======
import androidx.annotation.RequiresApi
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
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
<<<<<<< HEAD
    private val actionStop = "ACTION_STOP_SERVICE"

    @Suppress("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
            "MyApp::MusicPlayerWakelock")
        wakeLock.acquire(5*60*1000L) // 5 minutos
        val filter1 = IntentFilter(actionStop)
        val filter2 = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        }
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
=======

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
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
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
<<<<<<< HEAD
        //Detiene el estado de primer plano
        stopForeground(STOP_FOREGROUND_REMOVE)
        //Libera memoria del receiver
        unregisterReceiver(stopServiceReceiver)
        unregisterReceiver(bluetoothReceiver)
=======
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
        //Libera memoria del wakelock
        wakeLock.release()
        //Libera memoria del mediaplayer
        ObjectsManager.releaseMediaPlayer()
<<<<<<< HEAD
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
=======
        //Libera memoria del receiver
        unregisterReceiver(stopServiceReceiver)
        unregisterReceiver(bluetoothReceiver)
        //Detiene el estado de primer plano
        stopForeground(STOP_FOREGROUND_REMOVE)
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
    }

    private fun createNotification(
        musicName: String,
        artistName: String
    ): Notification {
<<<<<<< HEAD
        val iconId = R.mipmap.ic_launcher
=======
        val iconId = R.drawable.ic_launcher_foreground
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
        /* Ejecuta la app luego de dar click en la notificación
           Si la app esta cerrada la vuelve abrir */
        val openActivity = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java)
                .putExtra("IS_RESTART_APP", true),
<<<<<<< HEAD
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_IMMUTABLE
            else 0
=======
            PendingIntent.FLAG_IMMUTABLE
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
        )
        // Detiene el servicio al dar click en el boton Detener
        val stopService = PendingIntent.getBroadcast(
            this,
            0,
<<<<<<< HEAD
            Intent(actionStop),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                PendingIntent.FLAG_IMMUTABLE
            else 0
=======
            Intent("ACTION_STOP_SERVICE"),
            PendingIntent.FLAG_IMMUTABLE
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
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