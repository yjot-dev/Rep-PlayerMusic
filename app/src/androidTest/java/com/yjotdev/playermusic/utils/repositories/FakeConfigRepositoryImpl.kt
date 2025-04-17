package com.yjotdev.playermusic.utils.repositories

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.application.mvvm.model.RepeatOptions
import com.yjotdev.playermusic.domain.port.ConfigRepository

@Singleton
class FakeConfigRepositoryImpl @Inject constructor(): ConfigRepository {
    private val config = mutableMapOf<String, Any>()

    override fun saveConfig(
        valueRepeat: RepeatOptions, isShuffle: Boolean, isPlayList: Boolean,
        index0: Int, index1: Int, index2: Int
    ) {
        config["repeat"] = when(valueRepeat){
            RepeatOptions.Current -> 0
            RepeatOptions.All -> 1
            RepeatOptions.Off -> 2
        }
        config["isShuffle"] = isShuffle
        config["isPlayList"] = isPlayList
        config["index0"] = index0
        config["index1"] = index1
        config["index2"] = index2
    }

    override fun getConfig(): MutableMap<String, Any> {
        if(config.isEmpty()){
            config["repeat"] = 2
            config["isShuffle"] = false
            config["isPlayList"] = false
            config["index0"] = 0
            config["index1"] = 0
            config["index2"] = 0
        }
        return config
    }
}