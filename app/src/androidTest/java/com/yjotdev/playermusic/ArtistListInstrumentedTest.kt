package com.yjotdev.playermusic

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Rule
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import javax.inject.Inject
import dagger.hilt.android.testing.HiltAndroidRule
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
class ArtistListInstrumentedTest {

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

    @Test
    fun artistListViewNavigation() {
        composeTestRule.setContent {
            PlayerMusicTheme {
                PermissionView(
                    navController = navController
                )
            }
        }
        //Click en el 1er artista de la lista de artistas
        composeTestRule.onNodeWithTag("artist:0").performClick()
        //Navega a la lista de canciones del artista seleccionado
        assertEquals(ViewRoutes.MusicList.name, navController.currentDestination?.route)
        //Click en la 1ra cancion de la lista de musica
        composeTestRule.onNodeWithTag("music:0").performClick()
        //Navega a la vista de la cancion seleccionada
        assertEquals(ViewRoutes.CurrentMusic1.name, navController.currentDestination?.route)
    }

    @Test
    fun playMusic_ArtistListView() {
        artistListViewNavigation()
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
    fun nextMusic_ArtistListView() {
        artistListViewNavigation()
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
    fun previousMusic_ArtistListView() {
        artistListViewNavigation()
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

    @Test
    fun repeatMusic_ArtistListView() {
        artistListViewNavigation()
        //Click en el boton de repetir cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_repeat)
        ).performClick()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Deslizar slider casi al final para comprobar el repetir
        composeTestRule.onNodeWithTag("slider").performTouchInput {
            swipeRight(
                endX = right - 0.2f,
                durationMillis = 196000
            ) //3:16 minutos
        }
        //Espera 10 segundos para pausar la cancion
        runBlocking {
            delay(10000)
            composeTestRule.onNodeWithContentDescription(
                context.getString(R.string.cd_play)
            ).performClick()
        }
    }

    @Test
    fun shuffleMusic_ArtistListView() {
        artistListViewNavigation()
        //Click en el boton de aleatorio
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_shuffle)
        ).performClick()
        //Click en el boton de reproducir la cancion
        composeTestRule.onNodeWithContentDescription(
            context.getString(R.string.cd_play)
        ).performClick()
        //Deslizar slider casi al final para comprobar el aleatorio
        composeTestRule.onNodeWithTag("slider").performTouchInput {
            swipeRight(
                endX = right - 0.2f,
                durationMillis = 196000
            ) //3:16 minutos
        }
        //Espera 10 segundos para pausar la cancion
        runBlocking {
            delay(10000)
            composeTestRule.onNodeWithContentDescription(
                context.getString(R.string.cd_play)
            ).performClick()
        }
    }
}