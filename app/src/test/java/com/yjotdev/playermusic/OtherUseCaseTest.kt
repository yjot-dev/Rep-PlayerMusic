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
import com.yjotdev.playermusic.domain.entity.MusicListEntity
import com.yjotdev.playermusic.domain.entity.PlayerEntity
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.domain.port.ArtistListPort
import com.yjotdev.playermusic.domain.port.ConfigPort
import com.yjotdev.playermusic.domain.port.PlayerStatePort
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
        private lateinit var artistListPort: ArtistListPort
        private lateinit var getArtistListUseCase: GetArtistListUseCase

        @Before
        fun setUp() {
            artistListPort = mockk()
            getArtistListUseCase = GetArtistListUseCase(artistListPort)
        }

        @Test
        fun whenGetArtistListUseCaseIsInvokedThenItReturnsArtistList() = runTest {
            // Given
            val fakeArtistList = listOf(MusicListEntity(name = "Anuel AA", musicList = emptyList()))
            coEvery { artistListPort.getArtistMusicList() } returns fakeArtistList

            // When
            val result = getArtistListUseCase()

            // Then
            assertEquals(fakeArtistList, result)
            coVerify(exactly = 1) { artistListPort.getArtistMusicList() }
        }
    }

    /**
     * Pruebas para ConfigUseCase.
     */
    class ConfigUseCaseTest {
        private lateinit var configPort: ConfigPort
        private lateinit var configUseCase: ConfigUseCase

        @Before
        fun setUp() {
            configPort = mockk(relaxed = true)
            configUseCase = ConfigUseCase(configPort)
        }

        @Test
        fun whenConfigUseCaseIsInvokedToSaveThenPortSaveMethodIsCalled() {
            // Given
            val repeatValue = RepeatOptions.All
            val isPlaylist = true

            // When
            configUseCase.invoke(repeatValue, isPlaylist)

            // Then
            verify(exactly = 1) { configPort.saveConfig(repeatValue, isPlaylist) }
        }

        @Test
        fun whenConfigUseCaseIsInvokedToGetThenPortGetMethodReturnsConfig() {
            // Given
            val fakeConfig: MutableMap<String, Any> = mutableMapOf("repeat" to 1, "isPlayList" to true)
            every { configPort.getConfig() } returns fakeConfig

            // When
            val result = configUseCase.invoke()

            // Then
            assertEquals(fakeConfig, result)
            verify(exactly = 1) { configPort.getConfig() }
        }
    }

    /**
     * Pruebas para GetPlayerStateUseCase.
     */
    class PlayerStateUseCaseTest {
        private lateinit var playerStatePort: PlayerStatePort
        private lateinit var getPlayerStateUseCase: GetPlayerStateUseCase

        @Before
        fun setUp() {
            playerStatePort = mockk()
        }

        @Test
        fun whenGetPlayerStateUseCaseIsInvokedThenItReturnsPlayerStateFlow() {
            // Given
            val fakePlayerState = PlayerEntity()
            val stateFlow = MutableStateFlow(fakePlayerState)
            every { playerStatePort.playerState } returns stateFlow
            getPlayerStateUseCase = GetPlayerStateUseCase(playerStatePort)

            // When
            val result = getPlayerStateUseCase()

            // Then
            assertEquals(stateFlow, result)
            verify(exactly = 1) { playerStatePort.playerState }
        }
    }
}