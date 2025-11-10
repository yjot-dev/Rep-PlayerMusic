package com.yjotdev.playermusic.utils.repositories

import javax.inject.Singleton
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.ArtistListPort

@Singleton
class FakeArtistListRepository @Inject constructor(): ArtistListPort {
    private val artistListFlow = MutableStateFlow<List<MusicListEntity>>(emptyList())

    override suspend fun getArtistMusicList(): List<MusicListEntity> =
        artistListFlow.value

    /**
     * Permite a la clase de prueba "inyectar" la lista de artistas que se debe devolver.
     * @param artists La lista de artistas y sus canciones para la prueba.
     */
    fun setArtistList(artists: List<MusicListEntity>) {
        artistListFlow.value = artists
    }

    /**
     * Limpia los datos, simulando un estado sin música.
     */
    fun clearArtistList() {
        artistListFlow.value = emptyList()
    }
}