package com.yjotdev.playermusic.infrastructure.datasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yjotdev.playermusic.infrastructure.datasource.model.MusicListModel
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz DAO (Data Access Object) para Room.
 * Esta es la implementación concreta del puerto del dominio usando la tecnología Room.
 * Pertenece a la capa de Infraestructura.
 */
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