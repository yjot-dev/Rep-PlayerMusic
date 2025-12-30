package com.yjotdev.playermusic

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import com.yjotdev.playermusic.application.mvvm.viewModel.PlayerMusicViewModel
import com.yjotdev.playermusic.domain.entity.MusicEntity
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.entity.PlayerEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.domain.usecase.artist_list.GetArtistListUseCase
import com.yjotdev.playermusic.domain.usecase.config.ConfigUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.GetPlayerStateUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.NextTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PauseTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PlayTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.PreviousTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.ResumeTrackUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.SeekToUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.DeletePlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.GetPlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.InsertPlaylistUseCase
import com.yjotdev.playermusic.domain.usecase.playlist.UpdatePlaylistUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerMusicViewModelTest {

    // Dependencias Mockeadas
    private val insertPlayListUseCase: InsertPlaylistUseCase = mockk(relaxed = true)
    private val updatePlayListUseCase: UpdatePlaylistUseCase = mockk(relaxed = true)
    private val deletePlayListUseCase: DeletePlaylistUseCase = mockk(relaxed = true)
    private val getPlayListUseCase: GetPlaylistUseCase = mockk()
    private val getArtistListUseCase: GetArtistListUseCase = mockk()
    private val configUseCase: ConfigUseCase = mockk(relaxed = true)
    private val playTrackUseCase: PlayTrackUseCase = mockk(relaxed = true)
    private val pauseTrackUseCase: PauseTrackUseCase = mockk(relaxed = true)
    private val resumeTrackUseCase: ResumeTrackUseCase = mockk(relaxed = true)
    private val nextTrackUseCase: NextTrackUseCase = mockk(relaxed = true)
    private val previousTrackUseCase: PreviousTrackUseCase = mockk(relaxed = true)
    private val seekToUseCase: SeekToUseCase = mockk(relaxed = true)
    private val getPlayerStateUseCase: GetPlayerStateUseCase = mockk()

    // ViewModel bajo prueba
    private lateinit var viewModel: PlayerMusicViewModel

    // Configuración de Corrutinas
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Configuración por defecto de los mocks que se llaman en el init{} del ViewModel
        coEvery { getArtistListUseCase() } returns emptyList()
        every { getPlayListUseCase() } returns flowOf(emptyList())
        every { getPlayerStateUseCase() } returns MutableStateFlow(PlayerEntity())

        // Mock del ConfigUseCase (getConfig se llama en init)
        every { configUseCase.invoke() } returns mutableMapOf("repeat" to 0, "isPlayList" to false)

        viewModel = PlayerMusicViewModel(
            insertPlayListUseCase,
            updatePlayListUseCase,
            deletePlayListUseCase,
            getPlayListUseCase,
            getArtistListUseCase,
            configUseCase,
            playTrackUseCase,
            pauseTrackUseCase,
            resumeTrackUseCase,
            nextTrackUseCase,
            previousTrackUseCase,
            seekToUseCase,
            getPlayerStateUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadArtistListUpdatesStateOnInit() = runTest {
        // Given
        val mockArtists = listOf(MusicListEntity(name = "Artist 1", musicList = emptyList()))
        coEvery { getArtistListUseCase() } returns mockArtists

        // Re-instanciamos el VM para disparar el init con el nuevo mock
        viewModel = PlayerMusicViewModel(
            insertPlayListUseCase, updatePlayListUseCase, deletePlayListUseCase,
            getPlayListUseCase, getArtistListUseCase, configUseCase,
            playTrackUseCase, pauseTrackUseCase, resumeTrackUseCase, nextTrackUseCase,
            previousTrackUseCase, seekToUseCase, getPlayerStateUseCase
        )

        // When & Then
        viewModel.uiState.test {
            // El primer item puede ser el valor inicial vacío
            awaitItem()
            // Esperamos que se actualice con la lista de artistas
            val state = awaitItem()
            assertEquals(mockArtists, state.artistList)
        }
    }

    @Test
    fun setRepeatUpdatesRepeatOptionCorrectly() = runTest {
        // Usamos turbine para observar las emisiones del estado
        viewModel.uiState.test {
            // When: Value 0 -> Current
            viewModel.setRepeat(0)
            val updateState1 = awaitItem()
            assertEquals(RepeatOptions.Current, updateState1.repeat)

            // When: Value 1 -> All
            viewModel.setRepeat(1)
            val updateState2 = awaitItem()
            assertEquals(RepeatOptions.All, updateState2.repeat)

            // When: Value 2 (else) -> Shuffle
            viewModel.setRepeat(2)
            val updateState3 = awaitItem()
            assertEquals(RepeatOptions.Shuffle, updateState3.repeat)
        }
    }

    @Test
    fun toggleSongSelectionAddsAndRemovesSongs() = runTest {
        val song = MusicEntity(musicPath = "Path 1", musicName = "Song 1", artistName = "Artist A")

        // Usamos turbine para observar las emisiones del estado
        viewModel.uiState.test {
            // Estado inicial
            val initialState = awaitItem()
            assertFalse(initialState.itemSelected.contains(song))

            // Acción: Agregar canción
            viewModel.toggleSongSelection(song)

            // Esperamos la emisión del nuevo estado con la canción agregada
            val stateWithSong = awaitItem()
            assertTrue("La canción debería estar seleccionada", stateWithSong.itemSelected.contains(song))

            // Acción: Quitar canción
            viewModel.toggleSongSelection(song)

            // Esperamos la emisión del nuevo estado sin la canción
            val stateWithoutSong = awaitItem()
            assertFalse("La canción ya no debería estar seleccionada", stateWithoutSong.itemSelected.contains(song))
        }
    }

    @Test
    fun addMusicsToPlaylistCreatesNewPlaylistIfNameDoesNotExist() = runTest {
        // Usamos turbine para observar las emisiones del estado
        viewModel.uiState.test {
            // Consumir el estado inicial
            awaitItem()

            // Given
            val song = MusicEntity(musicPath = "Path 1", musicName = "Song 1", artistName = "Artist A")

            // Acción: Seleccionar canción
            viewModel.toggleSongSelection(song)

            // Verificar que el estado intermedio es correcto
            val stateWithSelection = awaitItem()
            assertTrue(stateWithSelection.itemSelected.contains(song))

            val newPlaylistName = "New Playlist"

            // When
            // Ahora ejecutamos la acción principal. Como ya consumimos el evento anterior,
            // estamos seguros de que el VM tiene la canción en memoria.
            val result = viewModel.addMusicsToPlaylist(newPlaylistName)
            // Espera a que la corutina finalice
            advanceUntilIdle()

            // Then
            assertEquals(2, result)
            coVerify {
                insertPlayListUseCase(match {
                    it.name == newPlaylistName && it.musicList.contains(song)
                })
            }

            // Consumir el evento de limpieza que ocurre después
            val finalState = awaitItem()
            assertTrue(finalState.itemSelected.isEmpty())
        }
    }

    @Test
    fun addMusicsToPlaylistUpdatesExistingPlaylist() = runTest {
        // Configuración PREVIA (Mocks y Re-instanciación)
        val existingPlaylist = MusicListEntity(name = "My Playlist", musicList = emptyList())

        // Mockeamos el flow para que devuelva la playlist existente
        every { getPlayListUseCase() } returns flowOf(listOf(existingPlaylist))

        // Re-instanciamos el VM AHORA para que tome el nuevo mock
        viewModel = PlayerMusicViewModel(
            insertPlayListUseCase, updatePlayListUseCase, deletePlayListUseCase,
            getPlayListUseCase, getArtistListUseCase, configUseCase,
            playTrackUseCase, pauseTrackUseCase, resumeTrackUseCase, nextTrackUseCase,
            previousTrackUseCase, seekToUseCase, getPlayerStateUseCase
        )

        // Ahora sí, abrimos el test sobre la instancia CORRECTA
        viewModel.uiState.test {
            // Consumir el estado inicial
            awaitItem()

            // Espera a que carge la playlist
            val stateWithPlaylist = awaitItem()
            assertTrue(stateWithPlaylist.playList.isNotEmpty())

            val song = MusicEntity(musicPath = "Path 2", musicName = "Song 2", artistName = "Artist B")

            // Acción: Seleccionar canción
            viewModel.toggleSongSelection(song)

            // Esperar actualización de selección
            val stateWithSelection = awaitItem()
            assertTrue(stateWithSelection.itemSelected.contains(song))

            // When: Agregar a playlist existente
            val result = viewModel.addMusicsToPlaylist("My Playlist")

            // Forzar ejecución de corrutinas pendientes (actualización de BD)
            advanceUntilIdle()

            // Then
            assertEquals(3, result) // 3 significa éxito actualizando existente

            coVerify {
                updatePlayListUseCase(match {
                    it.name == "My Playlist" && it.musicList.contains(song)
                })
            }

            // Consumir el evento de limpieza que ocurre después
            val finalState = awaitItem()
            assertTrue(finalState.itemSelected.isEmpty())
        }
    }

    @Test
    fun removeMusicsFromPlaylistUpdatesUseCase() = runTest {
        // Given
        val songToRemove = MusicEntity(musicPath = "Path 1", musicName = "Song 1", artistName = "Artist A")
        val songToKeep = MusicEntity(musicPath = "Path 2", musicName = "Song 2", artistName = "Artist B")
        val playlist = MusicListEntity(
            name = "Test List",
            musicList = listOf(songToRemove, songToKeep)
        )

        viewModel.uiState.test {
            // Consumir el estado inicial
            awaitItem()

            // Seleccionamos la canción a borrar
            viewModel.toggleSongSelection(songToRemove)

            // Esperar actualización de selección (Confirmamos que se seleccionó)
            val stateWithSelection = awaitItem()
            assertTrue(stateWithSelection.itemSelected.contains(songToRemove))

            // When: Ejecutamos la acción de borrar
            viewModel.removeMusicsFromPlaylist(playlist)

            // IMPORTANTE: Forzamos que la corrutina interna del ViewModel (updatePlayListUseCase) termine.
            // Si el ViewModel NO emite un nuevo estado aquí, Turbine se quedaba esperando y daba timeout.
            advanceUntilIdle()

            // Then 1: Verificamos que se llamó al caso de uso correctamente
            coVerify {
                updatePlayListUseCase(match { updatedList ->
                    !updatedList.musicList.contains(songToRemove) &&
                            updatedList.musicList.contains(songToKeep)
                })
            }

            // Then 2: Verificamos la limpieza visual.
            // Ahora esperamos el estado final limpio
            val finalState = awaitItem()
            assertTrue(finalState.itemSelected.isEmpty())
        }
    }

    @Test
    fun onPlayTrackCallsUseCaseWithCorrectList() = runTest {
        // Given
        val song = MusicEntity(musicPath = "Path 1", musicName = "Song 1", artistName = "Artist A")
        val artistList = MusicListEntity(name = "Artist", musicList = listOf(song))

        viewModel.setIsPlayList(false) // Modo Artista
        viewModel.setArtistListSelected(artistList)

        // When
        viewModel.onPlayTrack(song)

        // Then
        verify {
            playTrackUseCase(
                eq(song),
                eq(artistList.musicList),
                any()
            )
        }
    }

    @Test
    fun onSeekToConvertsSecondsToMilliseconds() {
        // Given
        val positionSeconds = 1.5f // 1.5 segundos

        // When
        viewModel.onSeekTo(positionSeconds)

        // Then
        verify { seekToUseCase(1500) } // Espera 1500 ms
    }

    @Test
    fun saveConfigCallsUseCaseWithCurrentState() {
        // Given
        viewModel.setRepeat(1) // RepeatOptions.All
        viewModel.setIsPlayList(true)

        // When
        viewModel.saveConfig()

        // Then
        verify {
            configUseCase.invoke(
                valueRepeat = RepeatOptions.All,
                isPlayList = true
            )
        }
    }
}