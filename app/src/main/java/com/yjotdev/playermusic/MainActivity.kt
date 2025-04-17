package com.yjotdev.playermusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

    private fun isRunningTest(): Boolean {
        return BuildConfig.DEBUG && Thread.currentThread().stackTrace.any {
            it.className.contains("androidx.test")
        }
    }
}