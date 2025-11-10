package com.yjotdev.playermusic.infrastructure.repositories

import javax.inject.Singleton
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.yjotdev.playermusic.domain.port.ConfigPort
import com.yjotdev.playermusic.domain.entity.RepeatOptions

@Singleton
class ConfigRepository @Inject constructor(
    @ApplicationContext val context: Context
): ConfigPort {
    private val sp = context.getSharedPreferences("MyConfig", Context.MODE_PRIVATE)

    /** Guarda datos persistentes del usuario **/
    override fun saveConfig(
        valueRepeat: RepeatOptions, isPlayList: Boolean
    ) {
        sp.edit().apply {
            putInt("repeat", when(valueRepeat){
                RepeatOptions.Current -> 0
                RepeatOptions.All -> 1
                RepeatOptions.Shuffle -> 2
            })
            putBoolean("isPlayList", isPlayList)
            apply()
        }
    }

    /** Obtiene datos persistentes del usuario **/
    override fun getConfig() = mutableMapOf<String, Any>(
        "repeat" to sp.getInt("repeat", 2),
        "isPlayList" to sp.getBoolean("isPlayList", false)
    )
}