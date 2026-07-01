package com.yjotdev.playermusic

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.After
import javax.inject.Inject
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import com.yjotdev.playermusic.presentation.navigation.PermissionView
import com.yjotdev.playermusic.presentation.navigation.ViewRoutes
import com.yjotdev.playermusic.presentation.theme.PlayerMusicTheme
import com.yjotdev.playermusic.domain.model.MusicModel
import com.yjotdev.playermusic.domain.model.MusicListModel
import com.yjotdev.playermusic.domain.repository.ArtistListRepository
import com.yjotdev.playermusic.utils.repositories.FakeArtistListRepositoryImpl
import com.yjotdev.playermusic.presentation.utils.TestTags

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ArtistListInstrumentedTest {

    @get:Rule(order = 0)
    var hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Inject
    lateinit var fakeArtistListRepository: ArtistListRepository // Inyectamos la interface del repositorio

    private lateinit var navController: TestNavHostController // NavController del Test
    private val context: Context = ApplicationProvider.getApplicationContext() // Contexto del test de la app

    @Before
    fun init() {
        hiltRule.inject() // Inicializa Hilt
        // Obtenemos los datos antes de iniciar los test
        val fakeData = fakeArtistListRepository as FakeArtistListRepositoryImpl
        fakeData.setArtistList(generateMockData())
    }

    @After
    fun tearDown(){
        // Limpiamos los datos después de finalizar los test
        val fakeData = fakeArtistListRepository as FakeArtistListRepositoryImpl
        fakeData.clearArtistList()
    }

    @Test
    fun navigationToCurrentMusic_ArtistListView() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())

            PlayerMusicTheme {
                PermissionView(navController = navController)
            }
        }
        //Espera a que carguen los datos
        composeTestRule.waitUntil(5000) {
            // Buscamos un nodo que solo existe cuando hay datos
            runCatching {
                composeTestRule.onNodeWithTag(TestTags.ARTIST_ITEM).assertIsDisplayed()
                true
            }.getOrDefault(false)
        }
        //Clic en el primer artista de la lista de artistas
        composeTestRule.onNodeWithTag(TestTags.ARTIST_ITEM).performClick()
        //Navega a la lista de canciones del artista seleccionado
        assertEquals(ViewRoutes.MusicList.name, navController.currentDestination?.route)
        //Clic en la 1ra canción de la lista de musica
        composeTestRule.onNodeWithTag(TestTags.MUSIC_ITEM).performClick()
        //Navega a la vista de la cancion seleccionada
        assertEquals(ViewRoutes.CurrentMusic1.name, navController.currentDestination?.route)
    }

    @Test
    fun playMusic_ArtistListView() {
        navigationToCurrentMusic_ArtistListView()
        //Clic en el botón de reproducir la canción
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la canción
        runBlocking { delay(5000.milliseconds) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }

    @Test
    fun nextMusic_ArtistListView() {
        navigationToCurrentMusic_ArtistListView()
        //Clic en el botón siguiente canción
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_next)
        ).performClick()
        //Clic en el botón de reproducir la canción
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la canción
        runBlocking { delay(5000.milliseconds) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }

    @Test
    fun previousMusic_ArtistListView() {
        navigationToCurrentMusic_ArtistListView()
        //Clic en el botón anterior canción
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_previous)
        ).performClick()
        //Clic en el botón de reproducir la canción
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la canción
        runBlocking { delay(5000.milliseconds) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }

    @Test
    fun repeatMusic_ArtistListView() {
        navigationToCurrentMusic_ArtistListView()
        //Repetir secuencialmente es la opción por defecto
        //Clic en el botón de reproducir la canción
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la canción
        runBlocking { delay(5000.milliseconds) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }

    @Test
    fun shuffleMusic_ArtistListView() {
        navigationToCurrentMusic_ArtistListView()
        //Repetir secuencialmente es la opción por defecto
        //Clic en el botón para repetir aleatoriamente todas las musicas
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_repeat)
        ).performClick()
        //Clic en el botón de reproducir la canción
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la canción
        runBlocking { delay(5000.milliseconds) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }

    private fun generateMockData(): List<MusicListModel>{
        val song1 = MusicModel(
            musicPath = "path/to/song1A",
            musicDuration = 200,
            musicName = "Song 1",
            artistName = "Artist A"
        )
        val song2 = MusicModel(
            musicPath = "path/to/song2A",
            musicDuration = 180,
            musicName = "Song 2",
            artistName = "Artist A"
        )
        val song3 = MusicModel(
            musicPath = "path/to/song3A",
            musicDuration = 150,
            musicName = "Song 3",
            artistName = "Artist A"
        )
        val song4 = MusicModel(
            musicPath = "path/to/song1B",
            musicDuration = 210,
            musicName = "Song 1",
            artistName = "Artist B"
        )
        val song5 = MusicModel(
            musicPath = "path/to/song2B",
            musicDuration = 190,
            musicName = "Song 2",
            artistName = "Artist B"
        )
        return listOf(
            MusicListModel(
                id = 0,
                name = "Artist A",
                musicList = listOf(song1, song2, song3),
                totalArtistAlbum = "2 albumen",
                totalArtistMusic = "3 canciones"
            ),
            MusicListModel(
                id = 1,
                name = "Artist B",
                musicList = listOf(song4, song5),
                totalArtistAlbum = "1 album",
                totalArtistMusic = "2 canciones"
            )
        )
    }
}