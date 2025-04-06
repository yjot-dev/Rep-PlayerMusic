package com.yjotdev.playermusic.infrastructure.repositories

import javax.inject.Singleton
import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.yjotdev.playermusic.domain.port.ConfigRepository
import com.yjotdev.playermusic.application.mvvm.model.RepeatOptions

@Singleton
class ConfigRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ConfigRepository {
    private val sp = context.getSharedPreferences("MyConfig", Context.MODE_PRIVATE)

    /** Guarda datos persistentes para botones aleatorio y repetir **/
    override fun saveConfig(
        valueRepeat: RepeatOptions, isShuffle: Boolean, isPlayList: Boolean,
        index0: Int, index1: Int, index2: Int
    ) {
        sp.edit().apply {
            putInt("repeat", when(valueRepeat){
                RepeatOptions.Current -> 0
                RepeatOptions.All -> 1
                RepeatOptions.Off -> 2
            })
            putBoolean("isShuffle", isShuffle)
            putBoolean("isPlayList", isPlayList)
            putInt("index0", index0)
            putInt("index1", index1)
            putInt("index2", index2)
            apply()
        }
    }

    /** Obtiene datos persistentes de botones aleatorio y repetir **/
    override fun getConfig(): SharedPreferences = sp
}