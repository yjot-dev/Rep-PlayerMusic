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
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.repository.PlaylistRepository
import com.yjotdev.playermusic.domain.usecase.playlist.DeletePlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.GetPlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.InsertPlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.UpdatePlaylistUseCase

/**
 * Pruebas unitarias para los casos de uso relacionados con las playlists.
 */
class PlaylistUseCaseTest {

    private lateinit var playlistRepository: PlaylistRepository
    private lateinit var getPlaylistUseCase: GetPlaylistUseCase
    private lateinit var insertPlaylistUseCase: InsertPlaylistUseCase
    private lateinit var updatePlaylistUseCase: UpdatePlaylistUseCase
    private lateinit var deletePlaylistUseCase: DeletePlaylistUseCase

    @Before
    fun setUp() {
        // Inicialización de mocks y casos de uso
        playlistRepository = mockk(relaxed = true)
        getPlaylistUseCase = GetPlaylistUseCase(playlistRepository)
        insertPlaylistUseCase = InsertPlaylistUseCase(playlistRepository)
        updatePlaylistUseCase = UpdatePlaylistUseCase(playlistRepository)
        deletePlaylistUseCase = DeletePlaylistUseCase(playlistRepository)
    }

    @Suppress("UnusedFlow")
    @Test
    fun whenGetPlaylistUseCaseIsInvokedThenItReturnsFlowOfPlaylists() = runTest {
        // Given
        val fakePlaylists = listOf(
            MusicListModel(id = 1, name = "Favorites", musicList = emptyList())
        )
        every { playlistRepository.getPlayList() } returns flowOf(fakePlaylists)

        // When
        val resultFlow = getPlaylistUseCase()
        val resultList = resultFlow.first()

        // Then
        assertEquals(fakePlaylists, resultList)
        verify(exactly = 1) { playlistRepository.getPlayList() }
    }

    @Test
    fun whenInsertPlaylistUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val newPlaylist = MusicListModel(name = "New Jams", musicList = emptyList())
        // coEvery para funciones suspendidas
        coEvery { playlistRepository.insertPlayList(newPlaylist) } returns Unit

        // When
        insertPlaylistUseCase(newPlaylist)

        // Then
        coVerify(exactly = 1) { playlistRepository.insertPlayList(newPlaylist) }
    }

    @Test
    fun whenUpdatePlaylistUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val updatedPlaylist = MusicListModel(id = 1, name = "Old Jams", musicList = emptyList())
        coEvery { playlistRepository.updatePlayList(updatedPlaylist) } returns Unit

        // When
        updatePlaylistUseCase(updatedPlaylist)

        // Then
        coVerify(exactly = 1) { playlistRepository.updatePlayList(updatedPlaylist) }
    }

    @Test
    fun whenDeletePlaylistUseCaseIsInvokedThenPortMethodIsCalled() = runTest {
        // Given
        val playlistToDelete = MusicListModel(id = 1, name = "To Delete", musicList = emptyList())
        coEvery { playlistRepository.deletePlayList(playlistToDelete) } returns Unit

        // When
        deletePlaylistUseCase(playlistToDelete)

        // Then
        coVerify(exactly = 1) { playlistRepository.deletePlayList(playlistToDelete) }
    }
}