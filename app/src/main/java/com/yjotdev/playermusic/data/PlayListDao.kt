package com.yjotdev.playermusic.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yjotdev.playermusic.ui.model.MusicListModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayListDao {
    @Insert
    suspend fun insertPlayList(item: MusicListModel)

    @Update
    suspend fun updatePlayList(item: MusicListModel)

    @Delete
    suspend fun deletePlayList(item: MusicListModel)

    @Query("SELECT * FROM playlist ORDER BY name ASC")
    fun getPlayList(): Flow<List<MusicListModel>>
}