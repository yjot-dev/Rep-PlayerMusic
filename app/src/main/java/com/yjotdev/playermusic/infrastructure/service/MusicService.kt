package com.yjotdev.playermusic.infrastructure.service

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
import javax.inject.Inject
import dagger.hilt.android.AndroidEntryPoint
import com.yjotdev.playermusic.MainActivity
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.infrastructure.repositories.PlayerStateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MusicService : Service() {
    @Inject lateinit var stateHolder: PlayerStateRepository
    private var progressJob: Job? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var currentPlaylist: List<MusicEntity> = emptyList()
    private var currentIndex: Int = -1
    private var repeatMode: RepeatOptions = RepeatOptions.All
    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var wakeLock: PowerManager.WakeLock
    private val notificationId = 1
    private val channelId = "MUSIC_CHANNEL"
    private val actionStop = "ACTION_STOP_SERVICE"

    @Suppress("UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        //Inicia el reproductor de música
        setupMediaPlayer()
        //Crea el wakelock para mantener la pantalla encendida
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
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
        }else {
            registerReceiver(stopServiceReceiver, filter1)
            registerReceiver(bluetoothReceiver, filter2)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> {
                val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra("TRACK", MusicEntity::class.java)
                } else {
                    intent.getParcelableExtra("TRACK")
                }
                val playlist = intent.getParcelableArrayListExtra<MusicEntity>("PLAYLIST")
                val repeat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getSerializableExtra("REPEAT_MODE", RepeatOptions::class.java)
                } else {
                    intent.getSerializableExtra("REPEAT_MODE")
                } as? RepeatOptions

                if (track != null && playlist != null && repeat != null) {
                    this.currentPlaylist = playlist
                    this.repeatMode = repeat
                    this.currentIndex = playlist.indexOf(track)
                    playTrack(track)
                }
            }
            "NEXT" -> nextTrackMediaPlayer()
            "PREVIOUS" -> previousTrackMediaPlayer()
            "PAUSE" -> {
                if(mediaPlayer.isPlaying){
                    mediaPlayer.pause()
                }
            }
            "RESUME" -> {
                if(!mediaPlayer.isPlaying){
                    mediaPlayer.start()
                }
            }
            "SEEK_TO" -> {
                val position = intent.getIntExtra("POSITION", 0)
                mediaPlayer.seekTo(position)
            }
        }

        // Extraemos la información para la notificación
        val currentTrack = if (currentIndex != -1) currentPlaylist[currentIndex] else null
        val trackName = currentTrack?.musicName ?: "Canción desconocida"
        val artistName = currentTrack?.artistName ?: "Artista desconocido"

        val notification = createNotification(trackName, artistName)
        startForeground(notificationId, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        //Limpia el scope del servicio
        serviceScope.cancel()
        //Detiene el estado de primer plano
        stopForeground(STOP_FOREGROUND_REMOVE)
        //Libera memoria del receiver
        unregisterReceiver(stopServiceReceiver)
        unregisterReceiver(bluetoothReceiver)
        //Libera memoria del wakelock
        wakeLock.release()
        //Libera memoria del MediaPlayer
        mediaPlayer.release()
    }
    // Proceso para iniciar reproductor de música
    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setOnCompletionListener {
            nextTrackMediaPlayer()
        }
    }
    // Proceso que reproduce la anterior cancion
    private fun previousTrackMediaPlayer() {
        if (currentPlaylist.isEmpty()) {
            stopSelf()
            return
        }
        val previousTrack = getPreviousTrack()
        if (previousTrack != null) {
            playTrack(previousTrack)
        } else {
            stopSelf()
        }
    }
    // Obtiene la anterior canción en función del modo de repetición
    private fun getPreviousTrack(): MusicEntity? {
        if (currentPlaylist.isEmpty()) return null

        return when (repeatMode) {
            RepeatOptions.Current -> currentPlaylist[currentIndex]
            RepeatOptions.All -> {
                val nextIndex = if (currentIndex - 1 < 0) currentPlaylist.size - 1 else currentIndex - 1
                currentPlaylist[nextIndex]
            }
            RepeatOptions.Shuffle -> {
                val randomIndex = (0 until currentPlaylist.size).random()
                currentPlaylist[randomIndex]
            }
        }
    }
    // Proceso que reproduce la siguiente cancion
    private fun nextTrackMediaPlayer() {
        if (currentPlaylist.isEmpty()) {
            stopSelf()
            return
        }
        val nextTrack = getNextTrack()
        if (nextTrack != null) {
            playTrack(nextTrack)
        } else {
            stopSelf()
        }
    }
    // Obtiene la siguiente canción en función del modo de repetición
    private fun getNextTrack(): MusicEntity? {
        if (currentPlaylist.isEmpty()) return null

        return when (repeatMode) {
            RepeatOptions.Current -> currentPlaylist[currentIndex]
            RepeatOptions.All -> {
                val nextIndex = if (currentIndex + 1 >= currentPlaylist.size) 0 else currentIndex + 1
                currentPlaylist[nextIndex]
            }
            RepeatOptions.Shuffle -> {
                val randomIndex = (0 until currentPlaylist.size).random()
                currentPlaylist[randomIndex]
            }
        }
    }
    // Proceso para preparar y reproducir la pista
    private fun playTrack(track: MusicEntity) {
        // Actualiza el índice actual
        this.currentIndex = currentPlaylist.indexOf(track)

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(track.musicPath)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                it.start()
                startProgressUpdates(track)
                // Actualiza la notificación con la nueva canción
                val notification = createNotification(track.musicName, track.artistName)
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(notificationId, notification)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startProgressUpdates(track: MusicEntity) {
        stopProgressUpdates()
        progressJob = serviceScope.launch {
            while (isActive) {
                stateHolder.updateState(
                    stateHolder.playerState.value.copy(
                        isPlaying = mediaPlayer.isPlaying,
                        currentTrack = track,
                        currentPosition = mediaPlayer.currentPosition,
                        totalDuration = mediaPlayer.duration,
                        hasCompleted = false
                    )
                )
                delay(500)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun channelNotification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//SDK >= 26
            val name = "Music Player"
            val descriptionText = "Notificaciones de reproducción de música"
            val importance = NotificationManager.IMPORTANCE_LOW
            val serviceChannel = NotificationChannel(
                channelId,
                name,
                importance
            ).apply {
                description = descriptionText
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(trackName: String, artistName: String): Notification {
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
            .setContentTitle(trackName.uppercase())
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
                    val pauseIntent = Intent(this@MusicService, MusicService::class.java).apply {
                        action = "PAUSE"
                    }
                    startService(pauseIntent)
                }
                //Reanuda reproductor de música si se ha conectado el bluetooth
                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    val resumeIntent = Intent(this@MusicService, MusicService::class.java).apply {
                        action = "RESUME"
                    }
                    startService(resumeIntent)
                }
            }
        }
    }
}