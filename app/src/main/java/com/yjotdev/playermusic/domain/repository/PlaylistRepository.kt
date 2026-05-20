package com.yjotdev.playermusic.domain.repository

import kotlinx.coroutines.flow.Flow
import com.yjotdev.playermusic.domain.model.MusicListModel

/**
 * Define el contrato (puerto) para las operaciones de datos relacionadas con la lista de reproducción.
 * Esta interfaz es pura y no conoce la fuente de datos (Room, API, etc.).
 * Pertenece a la capa de Dominio.
 */
interface PlaylistRepository {
    suspend fun insertPlayList(item: MusicListModel)

    suspend fun updatePlayList(item: MusicListModel)

    suspend fun deletePlayList(item: MusicListModel)

    fun getPlayList(): Flow<List<MusicListModel>>
}