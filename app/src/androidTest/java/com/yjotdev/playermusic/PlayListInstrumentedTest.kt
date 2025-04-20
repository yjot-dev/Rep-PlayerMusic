package com.yjotdev.playermusic

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yjotdev.playermusic.application.mvvm.viewModel.PlayerMusicViewModel
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import javax.inject.Inject
import dagger.hilt.android.testing.HiltAndroidTest
import com.yjotdev.playermusic.application.navigation.PermissionView
import com.yjotdev.playermusic.application.navigation.ViewRoutes
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PlayListInstrumentedTest {

    @get:Rule(order = 0)
    var hiltRule: HiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var navController: TestNavHostController // NavController del Test

    @Before
    fun setup() {
        hiltRule.inject() // Inicializa Hilt
    }
    // Contexto del test de la app.
    private val context: Context = ApplicationProvider.getApplicationContext()
    //Usa ViewModelProvider para obtener una instancia del ViewModel
    private lateinit var viewModel: PlayerMusicViewModel

    @Test
    fun playListViewNavigation() {
        viewModel = ViewModelProvider(composeTestRule.activity)[PlayerMusicViewModel::class.java]
        composeTestRule.setContent {
            PlayerMusicTheme {
                PermissionView(
                    navController = navController,
                    vmPlayerMusic = viewModel
                )
            }
        }
        //Espera a que cargen los datos
        composeTestRule.waitUntil(5000) {
            val state = viewModel.uiState.value
            state.uiArtistList.isNotEmpty()
        }
        addPlayList_PlayListView()
        //Click en el boton de playList
        composeTestRule.onNodeWithTag(
            context.getString(R.string.cd_navigation_playlist)
        ).performClick()
        //Navega a la lista de playlist
        assertEquals(ViewRoutes.PlayList.name, navController.currentDestination?.route)
        //Click en la 1ra playlist de la lista de playlist
        composeTestRule.onNodeWithTag("playList:0").performClick()
        //Navega a la lista de canciones de la playlist seleccionada
        assertEquals(ViewRoutes.CurrentPlayList.name, navController.currentDestination?.route)
        //Click en la 1ra cancion de la lista de musica
        composeTestRule.onNodeWithTag("music:0").performClick()
        //Navega a la vista de la cancion seleccionada
        assertEquals(ViewRoutes.CurrentMusic2.name, navController.currentDestination?.route)
    }

    private fun addPlayList_PlayListView() {
        //Click en el 1er artista de la lista de artistas
        composeTestRule.onNodeWithTag("artist:2").performClick()
        //Navega a la lista de canciones del artista seleccionado
        assertEquals(ViewRoutes.MusicList.name, navController.currentDestination?.route)
        //Ingresa una musica a la lista de reproduccion
        composeTestRule.onNodeWithTag("addPlayList:0").performClick()
        //Navega a la vista de ingreso de listas de reproduccion
        assertEquals(ViewRoutes.AddPlayList.name, navController.currentDestination?.route)
        //Escribe el nombre de la nueva playlist
        composeTestRule.onNodeWithTag("searchPlayList")
            .performTextInput("PlayList 1")
        //Click en el boton de agregar playlist
        composeTestRule.onNodeWithTag("addPlayList").performClick()
        //Verifica que regrese a la lista de canciones del artista seleccionado
        assertEquals(ViewRoutes.MusicList.name, navController.currentDestination?.route)
        //Navega a la lista de artistas
        composeTestRule.onNodeWithTag(context.getString(R.string.cd_navigation_back))
            .performClick()
        //Verifica que regrese a la lista de artistas
        assertEquals(ViewRoutes.ArtistList.name, navController.currentDestination?.route)
    }

    @Test
    fun playMusic_PlayListView() {
        playListViewNavigation()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la cancion
        runBlocking { delay(5000) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }

    @Test
    fun nextMusic_PlayListView() {
        playListViewNavigation()
        //Click en el boton siguiente cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_next)
        ).performClick()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la cancion
        runBlocking { delay(5000) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }

    @Test
    fun previousMusic_PlayListView() {
        playListViewNavigation()
        //Click en el boton anterior cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_previous)
        ).performClick()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la cancion
        runBlocking { delay(5000) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }

    @Test
    fun repeatMusic_PlayListView() {
        playListViewNavigation()
        //Click en el boton de repetir cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_repeat)
        ).performClick()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la cancion
        runBlocking { delay(5000) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }

    @Test
    fun shuffleMusic_PlayListView() {
        playListViewNavigation()
        //Click en el boton de aleatorio
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_shuffle)
        ).performClick()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 5 segundos para pausar la cancion
        runBlocking { delay(5000) }
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
    }
}