package com.yjotdev.playermusic.utils.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.repository.PlaylistRepository

@Singleton
class FakePlaylistRepositoryImpl @Inject constructor(): PlaylistRepository {
    // La "base de datos en memoria" es simplemente un StateFlow
    private val inMemoryPlaylists = MutableStateFlow<List<MusicListModel>>(emptyList())

    // --- Implementación de la interfaz ---
    override fun getPlayList(): Flow<List<MusicListModel>> {
        // Devuelve el Flow en memoria, no el del DAO.
        return inMemoryPlaylists.asStateFlow()
    }

    override suspend fun insertPlayList(item: MusicListModel) {
        inMemoryPlaylists.update { currentList ->
            // Asegura que no haya duplicados y añade el nuevo item.
            // Genera un ID si es necesario (para pruebas más realistas).
            if (currentList.any { it.id == item.id }) {
                currentList
            } else {
                currentList + item
            }
        }
    }

    override suspend fun updatePlayList(item: MusicListModel) {
        inMemoryPlaylists.update { currentList ->
            currentList.map { if (it.id == item.id) item else it }
        }
    }

    override suspend fun deletePlayList(item: MusicListModel) {
        inMemoryPlaylists.update { currentList ->
            currentList.filter { it.id != item.id }
        }
    }
}