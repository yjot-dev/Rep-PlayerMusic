package com.yjotdev.playermusic

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.port.PlaylistPort
import com.yjotdev.playermusic.domain.usecase.playlist.DeletePlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.GetPlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.InsertPlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.UpdatePlaylistUseCase

/**
 * Pruebas unitarias para los casos de uso relacionados con las playlists.
 */
class PlaylistUseCaseTest {

    private lateinit var playlistPort: PlaylistPort
    private lateinit var getPlaylistUseCase: GetPlaylistUseCase
    private lateinit var insertPlaylistUseCase: InsertPlaylistUseCase
    private lateinit var updatePlaylistUseCase: UpdatePlaylistUseCase
    private lateinit var deletePlaylistUseCase: DeletePlaylistUseCase

    @Before
    fun setUp() {
        // Inicialización de mocks y casos de uso
        playlistPort = mockk(relaxed = true)
        getPlaylistUseCase = GetPlaylistUseCase(playlistPort)
        insertPlaylistUseCase = InsertPlaylistUseCase(playlistPort)
        updatePlaylistUseCase = UpdatePlaylistUseCase(playlistPort)
        deletePlaylistUseCase = DeletePlaylistUseCase(playlistPort)
    }

    @Test
    fun whenGetPlaylistUseCaseIsInvokedThenItReturnsFlowOfPlaylists() = runTest {
        // Given
        val fakePlaylists = listOf(
            MusicListEntity(id = 1, name = "Favorites", musicList = emptyList())
        )
        every { playlistPort.getPlayList() } returns flowOf(fakePlaylists)

        // When
        val resultFlow = getPlaylistUseCase()
        val resultList = resultFlow.first()

        // Then
        assertEquals(fakePlaylists, resultList)
        verify(exactly = 1) { playlistPort.getPlayList() }
    }

    @Test
    fun whenInsertPlaylistUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val newPlaylist = MusicListEntity(name = "New Jams", musicList = emptyList())
        // coEvery para funciones suspendidas
        coEvery { playlistPort.insertPlayList(newPlaylist) } returns Unit

        // When
        insertPlaylistUseCase(newPlaylist)

        // Then
        coVerify(exactly = 1) { playlistPort.insertPlayList(newPlaylist) }
    }

    @Test
    fun whenUpdatePlaylistUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val updatedPlaylist = MusicListEntity(id = 1, name = "Old Jams", musicList = emptyList())
        coEvery { playlistPort.updatePlayList(updatedPlaylist) } returns Unit

        // When
        updatePlaylistUseCase(updatedPlaylist)

        // Then
        coVerify(exactly = 1) { playlistPort.updatePlayList(updatedPlaylist) }
    }

    @Test
    fun whenDeletePlaylistUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val playlistToDelete = MusicListEntity(id = 1, name = "To Delete", musicList = emptyList())
        coEvery { playlistPort.deletePlayList(playlistToDelete) } returns Unit

        // When
        deletePlaylistUseCase(playlistToDelete)

        // Then
        coVerify(exactly = 1) { playlistPort.deletePlayList(playlistToDelete) }
    }
}