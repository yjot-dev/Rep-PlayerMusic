package com.yjotdev.playermusic.infrastructure.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.port.PlayListPort
import com.yjotdev.playermusic.infrastructure.datasource.dao.PlayListDao
import com.yjotdev.playermusic.infrastructure.datasource.model.MusicListModel

@Singleton
class PlayListRepository @Inject constructor(
    private val playListDao: PlayListDao
): PlayListPort {
    private fun MusicListModel.toDomain(): MusicListEntity {
        return MusicListEntity(
            id = this.id,
            name = this.name,
            musicList = this.musicList,
            totalArtistMusic = totalArtistMusic(this.musicList),
            totalArtistAlbum = totalArtistAlbum(this.musicList)
        )
    }

    private fun MusicListEntity.toBD(): MusicListModel {
        return MusicListModel(
            id = this.id,
            name = this.name,
            musicList = this.musicList
        )
    }

    private fun totalArtistMusic(musicList: List<MusicEntity>) : String{
        return when(val res = musicList.count()){
            1 -> "$res canción"
            else -> "$res canciones"
        }
    }

    private fun totalArtistAlbum(musicList: List<MusicEntity>) : String{
        return when(val res = musicList.distinctBy{ it.albumName }.count()){
            1 -> "$res álbum"
            else -> "$res álbumes"
        }
    }

    /** Obtiene una lista de reproducción **/
    override fun getPlayList(): Flow<List<MusicListEntity>> {
        return playListDao.getPlayList().map { listFromDb ->
            listFromDb.map { model -> model.toDomain() }
        }
    }

    /** Inserta una lista de reproducción **/
    override suspend fun insertPlayList(item: MusicListEntity) {
        playListDao.insertPlayList(item.toBD())
    }

    /** Actualiza una lista de reproducción **/
    override suspend fun updatePlayList(item: MusicListEntity) {
        playListDao.updatePlayList(item.toBD())
    }

    /** Elimina una lista de reproducción **/
    override suspend fun deletePlayList(item: MusicListEntity) {
        playListDao.deletePlayList(item.toBD())
    }
}