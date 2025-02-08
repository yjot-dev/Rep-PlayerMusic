package com.yjotdev.playermusic.ui.viewModel

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri
import com.yjotdev.playermusic.R
import com.yjotdev.playermusic.data.ObjectsManager
import com.yjotdev.playermusic.ui.model.MusicListModel
import com.yjotdev.playermusic.ui.model.MusicModel
import com.yjotdev.playermusic.ui.model.RepeatOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaPlayerProcess {
    //Inicializa viewmodel
    private val mvPlayerMusic by lazy{ ObjectsManager.getVmPlayerMusic() }
    private val uiState = mvPlayerMusic.uiState
    //Inicializa reproductor de multimedia
    private val mp by lazy{ ObjectsManager.getMediaPlayer() }

    /** Convierte duración de Long a String formato minutos:segundos **/
    fun durationFormat(duration: Int) : String {
        var seconds = duration/1000
        val minutes = seconds/60
        seconds %= 60
        return if (seconds < 10) "$minutes:0$seconds"
        else "$minutes:$seconds"
    }
    /** Establece música actual en el MediaPlayer **/
    fun refreshMusic(){
        val index = uiState.value.uiCurrentMusicListIndex
        val list = uiState.value.uiMusicList[index]
        mp.reset()
        mp.setDataSource(list.musicPath)
        mp.prepare()
        mvPlayerMusic.setUiManualDurationValue(0)
        mvPlayerMusic.setUiIsPause(true)
    }
    /** Reproduce automaticamente la música **/
    private fun musicClicked(applicationContext: Context){
        val lastIndex = uiState.value.uiMusicList.lastIndex
        val indexRandom = (0..lastIndex).toList().shuffled()
        var repeat: RepeatOptions
        var shuffle: Boolean
        mp.setOnCompletionListener {
            repeat = uiState.value.uiRepeat
            shuffle = uiState.value.uiIsShuffle
            mvPlayerMusic.setUiIsCompletion(true)
            if(repeat == RepeatOptions.Current && !shuffle){
                //Si repetir la misma música esta activa
                mvPlayerMusic.setUiCurrentMusicListIndex(uiState.value.uiCurrentMusicListIndex)
                refreshMusic()
                playClicked(applicationContext)
            }else if(shuffle && repeat == RepeatOptions.Current){
                //Si ambos estan activos caso 1
                mvPlayerMusic.setUiCurrentMusicListIndex(uiState.value.uiCurrentMusicListIndex)
                refreshMusic()
                playClicked(applicationContext)
            }else{
                if (shuffle && repeat == RepeatOptions.All) {
                    //Si ambos estan activos caso 2
                    mvPlayerMusic.setUiCurrentMusicListIndex(indexRandom[uiState.value.uiCountIndex])
                    refreshMusic()
                    playClicked(applicationContext)
                    mvPlayerMusic.setUiCountIndex(uiState.value.uiCountIndex + 1)
                } else if (shuffle && repeat == RepeatOptions.Off) {
                    //Si aleatorio esta activo
                    mvPlayerMusic.setUiCurrentMusicListIndex(indexRandom[uiState.value.uiCountIndex])
                    refreshMusic()
                    playClicked(applicationContext)
                    mvPlayerMusic.setUiCountIndex(uiState.value.uiCountIndex + 1)
                } else if (repeat == RepeatOptions.All && !shuffle) {
                    //Si repetir toda la lista esta activa
                    mvPlayerMusic.setUiCurrentMusicListIndex(uiState.value.uiCurrentMusicListIndex + 1)
                    refreshMusic()
                    playClicked(applicationContext)
                } else mvPlayerMusic.setUiIsPause(true) //Si ninguno esta activo
            }
        }
    }
    /** Reproduce o pausa la música actual **/
    fun playClicked(applicationContext: Context){
        musicClicked(applicationContext)
        mvPlayerMusic.startServiceMusicPlayer(applicationContext)
        if (mp.isPlaying) {
            mp.pause()
            mvPlayerMusic.setUiIsPause(true)
        } else {
            mp.start()
            mvPlayerMusic.setUiIsPause(false)
            mvPlayerMusic.setUiAutoDurationValue()
        }
    }
    /** Obtiene imagen del album o deja imagen por defecto **/
    fun getAlbumUri(
        applicationContext: Context,
        albumImageUri: String
    ) : String{
        val albumArtExists = try{
            applicationContext.contentResolver.openInputStream(albumImageUri.toUri())?.close()
            true
        }
        catch (e: Exception) { false }
        return if(albumArtExists){ albumImageUri }
        else{
            Uri.parse("android.resource://" +
                    "${applicationContext.packageName}/" +
                    "${R.drawable.album_48}").toString()
        }
    }
    /** Obtiene música del dispositivo móvil del usuario **/
    suspend fun getArtistList(applicationContext: Context){
        //Realiza la consulta en un hilo de fondo para optimizar el rendimiento
        val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val subUri = Uri.parse("content://media/external/audio/albumart")
        //Define la información a obtener
        val projection = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val sortOrder = "${MediaStore.Audio.Media.TITLE} DESC"
        val artistList = mutableListOf<MusicListModel>()
        applicationContext.contentResolver.query(
            musicUri,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            //Obtiene la información de los audios del dispositivo móvil
            val musicPathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val musicDurationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val musicNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            while (cursor.moveToNext()) {
                val thisMusicPath = cursor.getString(musicPathColumn)
                val thisMusicDuration = cursor.getInt(musicDurationColumn)
                val thisMusicName = cursor.getString(musicNameColumn)
                val thisArtistName = cursor.getString(artistNameColumn)
                val thisAlbumName = cursor.getString(albumNameColumn)
                val thisAlbumId = cursor.getLong(albumIdColumn)
                val albumUri = ContentUris.withAppendedId(subUri, thisAlbumId).toString()
                val index = artistList.indexOfLast { it.name == thisArtistName }
                if (index == -1) {
                    //Si no hay el artista indicado
                    val musicList = listOf(
                        MusicModel(
                            musicPath = thisMusicPath,
                            musicDuration = thisMusicDuration,
                            musicDurationFormat = durationFormat(thisMusicDuration),
                            musicName = thisMusicName,
                            artistName = thisArtistName,
                            albumName = thisAlbumName,
                            albumUri = albumUri
                        )
                    )
                    artistList.add(
                        MusicListModel(
                            name = thisArtistName,
                            musicList = musicList
                        )
                    )
                } else {
                    //Si hay el artista indicado
                    val musicList = artistList[index].musicList.toMutableList()
                    musicList.add(
                        MusicModel(
                            musicPath = thisMusicPath,
                            musicDuration = thisMusicDuration,
                            musicDurationFormat = durationFormat(thisMusicDuration),
                            musicName = thisMusicName,
                            artistName = thisArtistName,
                            albumName = thisAlbumName,
                            albumUri = albumUri
                        )
                    )
                    artistList[index] = MusicListModel(
                        name = thisArtistName,
                        musicList = musicList
                    )
                }
            }
        }
        withContext(Dispatchers.Main){
            mvPlayerMusic.setUiArtistList(artistList)
        }
    }
}