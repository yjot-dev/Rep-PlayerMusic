package com.yjotdev.playermusic

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
import android.content.Intent
>>>>>>> master
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yjotdev.playermusic.data.ObjectsManager
<<<<<<< HEAD
<<<<<<< HEAD
=======
import com.yjotdev.playermusic.ui.model.RepeatOptions
>>>>>>> master
=======
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import com.yjotdev.playermusic.data.ObjectsManager
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
=======
import com.yjotdev.playermusic.service.MusicService
>>>>>>> master
=======
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yjotdev.playermusic.service.MusicService
>>>>>>> master
import com.yjotdev.playermusic.ui.theme.PlayerMusicTheme
import com.yjotdev.playermusic.ui.viewModel.DatabaseProcess

class MainActivity : ComponentActivity() {
<<<<<<< HEAD
    //Inicializa viewmodel
    private val vmPlayerMusic by lazy { ObjectsManager.getVmPlayerMusic() }
=======
>>>>>>> master
    //Inicializa DatabaseProcess
    private val dbProcess by lazy{ DatabaseProcess() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> master
=======
>>>>>>> master
        val isRestartApp = intent.getBooleanExtra("IS_RESTART_APP", false)
        setContent {
            PlayerMusicTheme {
                PermissionView(
<<<<<<< HEAD
                    vmPlayerMusic = vmPlayerMusic,
                    onProcess = {
                        vmPlayerMusic.setUiIsRestartApp(isRestartApp)
<<<<<<< HEAD
<<<<<<< HEAD
=======
                        //Recupero las configuraciones del usuario
>>>>>>> master
                        dbProcess.getData(applicationContext)
                    }
                )
=======
        if(getPermission()) {
            val isRestartApp = intent.getBooleanExtra("IS_RESTART_APP", false)
            vmPlayerMusic.setUiIsRestartApp(isRestartApp)
            dbProcess.getData(applicationContext)
        }
        setContent {
            PlayerMusicTheme {
                AppScreen(vmPlayerMusic = vmPlayerMusic)
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
=======
                        //Recupero las musicas del usuario
                        dbProcess.getData(applicationContext)
                    }
                )
>>>>>>> master
=======
                    isRestartApp = isRestartApp,
                    dbProcess = dbProcess
                )
>>>>>>> master
            }
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    override fun onPause() {
        super.onPause()
<<<<<<< HEAD
<<<<<<< HEAD
        val uiState = vmPlayerMusic.uiState.value
=======
        //Guardo las configuraciones del usuario
        val uiState = vmPlayerMusic.uiState.value
        dbProcess.saveConfig(applicationContext, valueRepeat = getValueRepeat(uiState.uiRepeat))
        dbProcess.saveConfig(applicationContext, isShuffle = uiState.uiIsShuffle)
>>>>>>> master
=======
        val uiState = vmPlayerMusic.uiState.value
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
        dbProcess.saveConfig(applicationContext, isPlayList = uiState.uiIsPlayList)
        dbProcess.saveConfig(applicationContext, index0 = uiState.uiCurrentArtistListIndex)
        dbProcess.saveConfig(applicationContext, index1 = uiState.uiCurrentPlayListIndex)
        dbProcess.saveConfig(applicationContext, index2 = uiState.uiCurrentMusicListIndex)
    }
<<<<<<< HEAD
<<<<<<< HEAD
=======

    private fun getValueRepeat(valueRepeat: RepeatOptions) : Int{
        return when(valueRepeat){
            RepeatOptions.Current -> 0
            RepeatOptions.All -> 1
            RepeatOptions.Off -> 2
        }
    }
>>>>>>> master
=======

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
>>>>>>> 6ffcaaca5d174609d25bb5b9aec7f445d66cf0b8
=======
=======
>>>>>>> master
    override fun onResume() {
        super.onResume()
        //Recupero las configuraciones del usuario
        dbProcess.getConfig(applicationContext)
    }

    override fun onPause() {
        super.onPause()
        //Avisa al servicio para guardar las configuraciones
        val intent = Intent(applicationContext, MusicService::class.java).apply {
            putExtra("isSaved", true)
        }
        startService(intent)
    }
<<<<<<< HEAD
>>>>>>> master
=======
>>>>>>> master
}