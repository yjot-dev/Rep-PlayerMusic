package com.yjotdev.playermusic

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.yjotdev.playermusic.data.ObjectsManager
import com.yjotdev.playermusic.ui.theme.PlayerMusicTheme
import com.yjotdev.playermusic.ui.viewModel.DatabaseProcess

class MainActivity : ComponentActivity() {
    //Inicializa viewmodel
    private val vmPlayerMusic by lazy { ObjectsManager.getVmPlayerMusic() }
    //Inicializa DatabaseProcess
    private val dbProcess by lazy{ DatabaseProcess() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(getPermission()) {
            val isRestartApp = intent.getBooleanExtra("IS_RESTART_APP", false)
            vmPlayerMusic.setUiIsRestartApp(isRestartApp)
            dbProcess.getData(applicationContext)
        }
        setContent {
            PlayerMusicTheme {
                AppScreen(vmPlayerMusic = vmPlayerMusic)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val uiState = vmPlayerMusic.uiState.value
        dbProcess.saveConfig(applicationContext, isPlayList = uiState.uiIsPlayList)
        dbProcess.saveConfig(applicationContext, index0 = uiState.uiCurrentArtistListIndex)
        dbProcess.saveConfig(applicationContext, index1 = uiState.uiCurrentPlayListIndex)
        dbProcess.saveConfig(applicationContext, index2 = uiState.uiCurrentMusicListIndex)
    }

    /** Solicita y verifica permisos **/
    private fun getPermission() : Boolean {
        val resultList = mutableListOf<Boolean>()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //Arreglo de permisos necesarios
            val permissionSdk33ToMore = arrayOf(
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.BLUETOOTH_CONNECT
            )
            //Solicta los permisos necesarios para la app
            ActivityCompat.requestPermissions(this, permissionSdk33ToMore,0)
            //Verifica que esten aprobados los permisos
            permissionSdk33ToMore.forEach { permission ->
                val check = ActivityCompat.checkSelfPermission(this, permission)
                resultList.add(check == PackageManager.PERMISSION_GRANTED)
            }
        }else{
            //Arreglo de permisos necesarios
            val permissionSdk24ToSdk32 = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            //Solicta los permisos necesarios para la app
            ActivityCompat.requestPermissions(this, permissionSdk24ToSdk32,0)
            //Verifica que esten aprobados los permisos
            permissionSdk24ToSdk32.forEach { permission ->
                val check = ActivityCompat.checkSelfPermission(this, permission)
                resultList.add(check == PackageManager.PERMISSION_GRANTED)
            }
        }
        return !resultList.contains(false)
    }
}