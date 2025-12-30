package com.yjotdev.playermusic.utils.repositories

import javax.inject.Singleton
import javax.inject.Inject
import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.domain.port.ConfigPort

@Singleton
class FakeConfigRepository @Inject constructor(): ConfigPort {
    private val config = mutableMapOf<String, Any>()

    override fun saveConfig(
        valueRepeat: RepeatOptions, isPlayList: Boolean
    ) {
        config["repeat"] = when(valueRepeat){
            RepeatOptions.Current -> 0
            RepeatOptions.All -> 1
            RepeatOptions.Shuffle -> 2
        }
        config["isPlayList"] = isPlayList
    }

    override fun getConfig(): MutableMap<String, Any> {
        if(config.isEmpty()){
            config["repeat"] = 1
            config["isPlayList"] = false
        }
        return config
    }
}