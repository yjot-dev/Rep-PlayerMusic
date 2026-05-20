package com.yjotdev.playermusic

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.model.PlayerModel
import com.yjotdev.playermusic.domain.utils.RepeatOptions
import com.yjotdev.playermusic.domain.repository.ArtistListRepository
import com.yjotdev.playermusic.domain.repository.ConfigRepository
import com.yjotdev.playermusic.domain.repository.PlayerStateRepository
import com.yjotdev.playermusic.domain.usecase.artist_list.GetArtistListUseCase
import com.yjotdev.playermusic.domain.usecase.config.ConfigUseCase
import com.yjotdev.playermusic.domain.usecase.media_player.GetPlayerStateUseCase

/**
 * Suite que agrupa pruebas para casos de uso de ArtistList, Config y PlayerState.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
    OtherUseCasesTest.ArtistListUseCaseTest::class,
    OtherUseCasesTest.ConfigUseCaseTest::class,
    OtherUseCasesTest.PlayerStateUseCaseTest::class
)
class OtherUseCasesTest {

    /**
     * Pruebas para GetArtistListUseCase.
     */
    class ArtistListUseCaseTest {
        private lateinit var artistListRepository: ArtistListRepository
        private lateinit var getArtistListUseCase: GetArtistListUseCase

        @Before
        fun setUp() {
            artistListRepository = mockk()
            getArtistListUseCase = GetArtistListUseCase(artistListRepository)
        }

        @Test
        fun whenGetArtistListUseCaseIsInvokedThenItReturnsArtistList() = runTest {
            // Given
            val fakeArtistList = listOf(MusicListModel(name = "Anuel AA", musicList = emptyList()))
            coEvery { artistListRepository.getArtistMusicList() } returns fakeArtistList

            // When
            val result = getArtistListUseCase()

            // Then
            assertEquals(fakeArtistList, result)
            coVerify(exactly = 1) { artistListRepository.getArtistMusicList() }
        }
    }

    /**
     * Pruebas para ConfigUseCase.
     */
    class ConfigUseCaseTest {
        private lateinit var configRepository: ConfigRepository
        private lateinit var configUseCase: ConfigUseCase

        @Before
        fun setUp() {
            configRepository = mockk(relaxed = true)
            configUseCase = ConfigUseCase(configRepository)
        }

        @Test
        fun whenConfigUseCaseIsInvokedToSaveThenPortSaveMethodIsCalled() {
            // Given
            val repeatValue = RepeatOptions.All
            val isPlaylist = true

            // When
            configUseCase.invoke(repeatValue, isPlaylist)

            // Then
            verify(exactly = 1) { configRepository.saveConfig(repeatValue, isPlaylist) }
        }

        @Test
        fun whenConfigUseCaseIsInvokedToGetThenPortGetMethodReturnsConfig() {
            // Given
            val fakeConfig: MutableMap<String, Any> = mutableMapOf("repeat" to 1, "isPlayList" to true)
            every { configRepository.getConfig() } returns fakeConfig

            // When
            val result = configUseCase.invoke()

            // Then
            assertEquals(fakeConfig, result)
            verify(exactly = 1) { configRepository.getConfig() }
        }
    }

    /**
     * Pruebas para GetPlayerStateUseCase.
     */
    class PlayerStateUseCaseTest {
        private lateinit var playerStateRepository: PlayerStateRepository
        private lateinit var getPlayerStateUseCase: GetPlayerStateUseCase

        @Before
        fun setUp() {
            playerStateRepository = mockk()
        }

        @Test
        fun whenGetPlayerStateUseCaseIsInvokedThenItReturnsPlayerStateFlow() {
            // Given
            val fakePlayerState = PlayerModel()
            val stateFlow = MutableStateFlow(fakePlayerState)
            every { playerStateRepository.playerState } returns stateFlow
            getPlayerStateUseCase = GetPlayerStateUseCase(playerStateRepository)

            // When
            val result = getPlayerStateUseCase()

            // Then
            assertEquals(stateFlow, result)
            verify(exactly = 1) { playerStateRepository.playerState }
        }
    }
}