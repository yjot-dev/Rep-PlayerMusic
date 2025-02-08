package com.yjotdev.playermusic

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.yjotdev.playermusic.data.ObjectsManager
import com.yjotdev.playermusic.ui.theme.PlayerMusicTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class PlayListInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    // Contexto del test de la app.
    private val context: Context = ApplicationProvider.getApplicationContext()
    //Inicializa viewmodel
    private val vmPlayerMusic by lazy { ObjectsManager.getVmPlayerMusic() }

    @Test
    fun playListViewNavigation() {
        // NavController del Test
        val navController = TestNavHostController(context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
        }
        composeTestRule.setContent {
            PlayerMusicTheme {
                PermissionView(
                    vmPlayerMusic = vmPlayerMusic,
                    navController = navController
                )
            }
        }
        //Click en el boton de playList
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_navigation_playlist)
        ).performClick()
        //Navega a la lista de playlist
        assertEquals(RouteViews.PlayList.name, navController.currentDestination?.route)
        //Click en la 1ra playlist de la lista de playlist
        composeTestRule.onNodeWithTag("playList:0").performClick()
        //Navega a la lista de canciones de la playlist seleccionada
        assertEquals(RouteViews.CurrentPlayList.name, navController.currentDestination?.route)
        //Click en la 1ra cancion de la lista de musica
        composeTestRule.onNodeWithTag("music:0").performClick()
        //Navega a la vista de la cancion seleccionada
        assertEquals(RouteViews.CurrentMusic2.name, navController.currentDestination?.route)
    }


    @Test
    fun playMusic_playListView() {
        playListViewNavigation()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 10 segundos para pausar la cancion
        runBlocking {
            delay(10000)
            composeTestRule.onNodeWithContentDescription(
                context.getString(R.string.cd_play)
            ).performClick()
        }
    }

    @Test
    fun nextMusic_playListView() {
        playListViewNavigation()
        //Click en el boton siguiente cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_next)
        ).performClick()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 10 segundos para pausar la cancion
        runBlocking {
            delay(10000)
            composeTestRule.onNodeWithContentDescription(
                context.getString(R.string.cd_play)
            ).performClick()
        }
    }

    @Test
    fun previousMusic_playListView() {
        playListViewNavigation()
        //Click en el boton anterior cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_previous)
        ).performClick()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Espera 10 segundos para pausar la cancion
        runBlocking {
            delay(10000)
            composeTestRule.onNodeWithContentDescription(
                context.getString(R.string.cd_play)
            ).performClick()
        }
    }
}