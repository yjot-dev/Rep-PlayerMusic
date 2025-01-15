package com.yjotdev.playermusic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.yjotdev.playermusic.ui.viewModel.PlayerMusicViewModel

@Composable
fun PermissionView(
    vmPlayerMusic: PlayerMusicViewModel,
    onProcess: () -> Unit
){
    val context = LocalContext.current
    var hasPermissions by remember{ mutableStateOf(checkPermissions(context)) }
    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
        hasPermissions = permissions.all { permission -> permission.value }
    }
    if(hasPermissions){
        onProcess()
        AppScreen(vmPlayerMusic = vmPlayerMusic)
    }else{
        // Solicitar permisos al usuario
        LaunchedEffect(Unit){
            requestPermissionsLauncher.launch(getPermission())
        }
    }
}

/** Verifica los permisos **/
private fun checkPermissions(context: Context): Boolean {
    return getPermission().all { permission ->
        ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}

/** Obtiene los permisos **/
private fun getPermission() : Array<String>{
    val permissionArray: Array<String>
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){ //SDK >= 33
        //Arreglo de permisos necesarios
        permissionArray = arrayOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){ //SDK >= 31
        //Arreglo de permisos necesarios
        permissionArray = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    }else{
        //Arreglo de permisos necesarios
        permissionArray = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
    return permissionArray
}