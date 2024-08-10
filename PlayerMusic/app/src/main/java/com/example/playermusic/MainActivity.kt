package com.example.playermusic

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.example.playermusic.service.MusicService
import com.example.playermusic.ui.model.MusicListModel
import com.example.playermusic.ui.model.MusicModel
import com.example.playermusic.ui.theme.PlayerMusicTheme
import com.example.playermusic.ui.viewModel.PlayerMusicViewModel

class MainActivity : ComponentActivity() {

    private val vmPlayerMusic by lazy { PlayerMusicViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getPermission()
        refreshArtistList()
        setContent {
            PlayerMusicTheme(darkTheme = false) {
                AppScreen(vmPlayerMusic = vmPlayerMusic)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getPermission()
        refreshArtistList()
    }

    override fun onDestroy() {
        super.onDestroy()
        //Cerrar servicio
        val serviceIntent = Intent(applicationContext, MusicService::class.java)
        stopService(serviceIntent)
        //Cerrar la aplicación
        finishAffinity()
    }

    private fun getPermission() {
        val permissionRequired = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        //Solicita permisos
        ActivityCompat.requestPermissions(this,permissionRequired,0)
    }

    /** Metodos de obtención de música del usuario **/
    private fun refreshArtistList(){
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
        contentResolver.query(
            musicUri,
            projection,
            null,
            null,
            sortOrder
        )?.use{ cursor ->
            //Obtiene la información de los audios del dispositivo móvil
            val musicPathColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val musicDurationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
            val musicNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val albumNameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            while(cursor.moveToNext()){
                val thisMusicPath = cursor.getString(musicPathColumn)
                val thisMusicDuration = cursor.getInt(musicDurationColumn)
                val thisMusicName = cursor.getString(musicNameColumn)
                val thisArtistName = cursor.getString(artistNameColumn)
                val thisAlbumName = cursor.getString(albumNameColumn)
                val thisAlbumId = cursor.getLong(albumIdColumn)
                val albumUri = ContentUris.withAppendedId(subUri, thisAlbumId).toString()
                val index = artistList.indexOfLast { it.name == thisArtistName }
                if(index == -1){
                    //Si no hay el artista indicado
                    val musicList = listOf(
                        MusicModel(
                            musicPath = thisMusicPath,
                            musicDuration = thisMusicDuration,
                            musicName = thisMusicName,
                            artistName = thisArtistName,
                            albumName = thisAlbumName,
                            albumUri = albumUri
                        ))
                    artistList.add(MusicListModel(
                        name = thisArtistName,
                        musicList = musicList
                    ))
                }else{
                    //Si hay el artista indicado
                    val musicList = artistList[index].musicList.toMutableList()
                    musicList.add(
                        MusicModel(
                            musicPath = thisMusicPath,
                            musicDuration = thisMusicDuration,
                            musicName = thisMusicName,
                            artistName = thisArtistName,
                            albumName = thisAlbumName,
                            albumUri = albumUri
                        ))
                    artistList[index] = MusicListModel(
                        name = thisArtistName,
                        musicList = musicList
                    )
                }
            }
        }
        vmPlayerMusic.setUiArtistList(artistList)
    }
}