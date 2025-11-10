package com.yjotdev.playermusic.infrastructure.repositories

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.yjotdev.playermusic.domain.port.ArtistListPort
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.utils.Helper.getTotalArtistAlbum
import com.yjotdev.playermusic.domain.utils.Helper.getTotalArtistMusic

@Singleton
class ArtistListRepository @Inject constructor(
    @ApplicationContext val context: Context
): ArtistListPort {
    override suspend fun getArtistMusicList(): List<MusicListEntity> {
        return withContext(Dispatchers.IO) {
            //Realiza la consulta en un hilo de fondo para optimizar el rendimiento
            val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val subUri = "content://media/external/audio/albumart".toUri()
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
            val artistList = mutableListOf<MusicListEntity>()
            context.contentResolver.query(
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
                            MusicEntity(
                                musicPath = thisMusicPath,
                                musicDuration = thisMusicDuration,
                                musicName = thisMusicName,
                                artistName = thisArtistName,
                                albumName = thisAlbumName,
                                albumUri = albumUri
                            )
                        )
                        artistList.add(
                            MusicListEntity(
                                name = thisArtistName,
                                musicList = musicList,
                                totalArtistMusic = getTotalArtistMusic(musicList),
                                totalArtistAlbum = getTotalArtistAlbum(musicList)
                            )
                        )
                    } else {
                        //Si hay el artista indicado
                        val musicList = artistList[index].musicList.toMutableList()
                        musicList.add(
                            MusicEntity(
                                musicPath = thisMusicPath,
                                musicDuration = thisMusicDuration,
                                musicName = thisMusicName,
                                artistName = thisArtistName,
                                albumName = thisAlbumName,
                                albumUri = albumUri
                            )
                        )
                        artistList[index] = MusicListEntity(
                            name = thisArtistName,
                            musicList = musicList,
                            totalArtistMusic = getTotalArtistMusic(musicList),
                            totalArtistAlbum = getTotalArtistAlbum(musicList)
                        )
                    }
                }
            }
            artistList
        }
    }
}