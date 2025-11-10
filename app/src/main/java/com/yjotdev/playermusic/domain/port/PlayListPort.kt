package com.yjotdev.playermusic.domain.port

import kotlinx.coroutines.flow.Flow
import com.yjotdev.playermusic.domain.entity.MusicListEntity

/**
 * Define el contrato (puerto) para las operaciones de datos relacionadas con la lista de reproducción.
 * Esta interfaz es pura y no conoce la fuente de datos (Room, API, etc.).
 * Pertenece a la capa de Dominio.
 */
interface PlayListPort {
    suspend fun insertPlayList(item: MusicListEntity)

    suspend fun updatePlayList(item: MusicListEntity)

    suspend fun deletePlayList(item: MusicListEntity)

    fun getPlayList(): Flow<List<MusicListEntity>>
}