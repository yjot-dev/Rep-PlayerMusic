package com.yjotdev.playermusic.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.repository.PlaylistRepository
import com.yjotdev.playermusic.data.local.dao.PlaylistDao
import com.yjotdev.playermusic.data.local.mapper.toDomain
import com.yjotdev.playermusic.data.local.mapper.toBD

@Singleton
class PlaylistRepositoryImpl @Inject constructor(
    private val playListDao: PlaylistDao
): PlaylistRepository {
    /** Obtiene una lista de reproducción **/
    override fun getPlayList(): Flow<List<MusicListModel>> {
        return playListDao.getPlayList().map { listFromDb ->
            listFromDb.map { model -> model.toDomain() }
        }
    }

    /** Inserta una lista de reproducción **/
    override suspend fun insertPlayList(item: MusicListModel) {
        playListDao.insertPlayList(item.toBD())
    }

    /** Actualiza una lista de reproducción **/
    override suspend fun updatePlayList(item: MusicListModel) {
        playListDao.updatePlayList(item.toBD())
    }

    /** Elimina una lista de reproducción **/
    override suspend fun deletePlayList(item: MusicListModel) {
        playListDao.deletePlayList(item.toBD())
    }
}