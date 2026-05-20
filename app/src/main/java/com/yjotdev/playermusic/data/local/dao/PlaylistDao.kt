package com.yjotdev.playermusic.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yjotdev.playermusic.data.local.entity.MusicListEntity
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz DAO (Data Access Object) para Room.
 * Esta es la implementación concreta del puerto del dominio usando la tecnología Room.
 * Pertenece a la capa de Infraestructura.
 */
@Dao
interface PlaylistDao {

    @Insert
    suspend fun insertPlayList(item: MusicListEntity)

    @Update
    suspend fun updatePlayList(item: MusicListEntity)

    @Delete
    suspend fun deletePlayList(item: MusicListEntity)

    @Query("SELECT * FROM playlist ORDER BY name ASC")
    fun getPlayList(): Flow<List<MusicListEntity>>
}