package com.yjotdev.playermusic.domain.port

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.yjotdev.playermusic.domain.entity.MusicListEntity

@Dao
interface PlayListRepository {
    @Insert
    suspend fun insertPlayList(item: MusicListEntity)

    @Update
    suspend fun updatePlayList(item: MusicListEntity)

    @Delete
    suspend fun deletePlayList(item: MusicListEntity)

    @Query("SELECT * FROM playlist ORDER BY name ASC")
    fun getPlayList(): Flow<List<MusicListEntity>>
}