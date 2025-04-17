package com.yjotdev.playermusic.utils.repositories

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.PlayListRepository
import com.yjotdev.playermusic.infrastructure.adapter.PlayListDatabase

@Singleton
class FakePlayListRepositoryImpl @Inject constructor(
    private val db: PlayListDatabase
): PlayListRepository {
    override suspend fun insertPlayList(item: MusicListEntity) {
        db.playListDao().insertPlayList(item)
    }

    override suspend fun updatePlayList(item: MusicListEntity) {
        db.playListDao().updatePlayList(item)
    }

    override suspend fun deletePlayList(item: MusicListEntity) {
        db.playListDao().deletePlayList(item)
    }

    override fun getPlayList(): Flow<List<MusicListEntity>> {
        return db.playListDao().getPlayList()
    }
}