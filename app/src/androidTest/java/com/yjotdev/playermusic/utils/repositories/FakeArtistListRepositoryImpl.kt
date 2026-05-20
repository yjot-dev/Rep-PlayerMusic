package com.yjotdev.playermusic.utils.repositories

import javax.inject.Singleton
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.repository.ArtistListRepository

@Singleton
class FakeArtistListRepositoryImpl @Inject constructor(): ArtistListRepository {
    private val artistListFlow = MutableStateFlow<List<MusicListModel>>(emptyList())

    override suspend fun getArtistMusicList(): List<MusicListModel> =
        artistListFlow.value

    /**
     * Permite a la clase de prueba "inyectar" la lista de artistas que se debe devolver.
     * @param artists La lista de artistas y sus canciones para la prueba.
     */
    fun setArtistList(artists: List<MusicListModel>) {
        artistListFlow.value = artists
    }

    /**
     * Limpia los datos, simulando un estado sin música.
     */
    fun clearArtistList() {
        artistListFlow.value = emptyList()
    }
}