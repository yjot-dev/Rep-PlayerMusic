package com.yjotdev.playermusic

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yjotdev.playermusic.service.MusicService
import com.yjotdev.playermusic.ui.theme.PlayerMusicTheme
import com.yjotdev.playermusic.ui.viewModel.DatabaseProcess

class MainActivity : ComponentActivity() {
    //Inicializa DatabaseProcess
    private val dbProcess by lazy{ DatabaseProcess() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isRestartApp = intent.getBooleanExtra("IS_RESTART_APP", false)
        setContent {
            PlayerMusicTheme {
                PermissionView(
                    isRestartApp = isRestartApp,
                    dbProcess = dbProcess
                )
            }
        }
    }

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
}