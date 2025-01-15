package com.yjotdev.playermusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yjotdev.playermusic.data.ObjectsManager
import com.yjotdev.playermusic.ui.model.RepeatOptions
import com.yjotdev.playermusic.ui.theme.PlayerMusicTheme
import com.yjotdev.playermusic.ui.viewModel.DatabaseProcess

class MainActivity : ComponentActivity() {
    //Inicializa viewmodel
    private val vmPlayerMusic by lazy { ObjectsManager.getVmPlayerMusic() }
    //Inicializa DatabaseProcess
    private val dbProcess by lazy{ DatabaseProcess() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isRestartApp = intent.getBooleanExtra("IS_RESTART_APP", false)
        setContent {
            PlayerMusicTheme {
                PermissionView(
                    vmPlayerMusic = vmPlayerMusic,
                    onProcess = {
                        vmPlayerMusic.setUiIsRestartApp(isRestartApp)
                        //Recupero las configuraciones del usuario
                        dbProcess.getData(applicationContext)
                    }
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        //Guardo las configuraciones del usuario
        val uiState = vmPlayerMusic.uiState.value
        dbProcess.saveConfig(applicationContext, valueRepeat = getValueRepeat(uiState.uiRepeat))
        dbProcess.saveConfig(applicationContext, isShuffle = uiState.uiIsShuffle)
        dbProcess.saveConfig(applicationContext, isPlayList = uiState.uiIsPlayList)
        dbProcess.saveConfig(applicationContext, index0 = uiState.uiCurrentArtistListIndex)
        dbProcess.saveConfig(applicationContext, index1 = uiState.uiCurrentPlayListIndex)
        dbProcess.saveConfig(applicationContext, index2 = uiState.uiCurrentMusicListIndex)
    }

    private fun getValueRepeat(valueRepeat: RepeatOptions) : Int{
        return when(valueRepeat){
            RepeatOptions.Current -> 0
            RepeatOptions.All -> 1
            RepeatOptions.Off -> 2
        }
    }
}