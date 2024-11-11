package com.example.playermusic

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.playermusic.data.ObjectsManager
import com.example.playermusic.ui.theme.PlayerMusicTheme
import com.example.playermusic.ui.viewModel.DatabaseProcess

class MainActivity : ComponentActivity() {
    //Inicializa viewmodel
    private val vmPlayerMusic by lazy { ObjectsManager.getVmPlayerMusic() }
    //Inicializa DatabaseProcess
    private val dbProcess by lazy{ DatabaseProcess() }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getPermission()
        if(intent.getBooleanExtra("IS_RESTART_APP", false)){
            dbProcess.saveConfig(applicationContext, isRestartApp = true)
        }else{
            dbProcess.saveConfig(applicationContext, isRestartApp = false)
        }
        dbProcess.getData(applicationContext)

        setContent {
            PlayerMusicTheme {
                AppScreen(vmPlayerMusic = vmPlayerMusic)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onResume() {
        super.onResume()
        getPermission()
    }

    override fun onPause() {
        super.onPause()
        val uiState = vmPlayerMusic.uiState.value
        dbProcess.saveConfig(applicationContext, isPlayList = uiState.uiIsPlayList)
        dbProcess.saveConfig(applicationContext, index0 = uiState.uiCurrentArtistListIndex)
        dbProcess.saveConfig(applicationContext, index1 = uiState.uiCurrentPlayListIndex)
        dbProcess.saveConfig(applicationContext, index2 = uiState.uiCurrentMusicListIndex)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun getPermission() {
        val permissionRequired = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH_CONNECT
        )
        //Solicita permisos
        ActivityCompat.requestPermissions(this,permissionRequired,0)
    }
}