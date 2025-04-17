package com.yjotdev.playermusic.domain.port

import com.yjotdev.playermusic.application.mvvm.model.RepeatOptions

interface ConfigRepository {

    fun saveConfig(valueRepeat: RepeatOptions, isShuffle: Boolean, isPlayList: Boolean,
                   index0: Int, index1: Int, index2: Int)

    fun getConfig(): MutableMap<String, Any>
}