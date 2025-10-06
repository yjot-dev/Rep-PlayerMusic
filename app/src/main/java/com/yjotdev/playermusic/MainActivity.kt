package com.yjotdev.playermusic

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import com.yjotdev.playermusic.application.navigation.PermissionView
import com.yjotdev.playermusic.application.theme.PlayerMusicTheme
import com.yjotdev.playermusic.application.mvvm.viewModel.PlayerMusicViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var vmPlayerMusic: PlayerMusicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ajusta la vista a toda la pantalla
        viewEdgeToEdge()
        //Inicializo el ViewModel
        vmPlayerMusic = ViewModelProvider(this)[PlayerMusicViewModel::class.java]
        //Verifico si se reinició la app
        val isRestartApp = intent.getBooleanExtra("IS_RESTART_APP", false)
        if (!isRunningTest()){
            setContent {
                PlayerMusicTheme {
                    PermissionView(
                        vmPlayerMusic = vmPlayerMusic,
                        isRestartApp = isRestartApp
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        vmPlayerMusic.getMusicList()
        vmPlayerMusic.getConfig()
    }

    private fun viewEdgeToEdge(){
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb()
            )
        )
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            }
        }
    }

    private fun isRunningTest(): Boolean {
        return BuildConfig.DEBUG && Thread.currentThread().stackTrace.any {
            it.className.contains("androidx.test")
        }
    }
}