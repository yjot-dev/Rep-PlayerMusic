package com.yjotdev.playermusic.domain.port

import com.yjotdev.playermusic.domain.entity.RepeatOptions

interface ConfigPort {

    fun saveConfig(valueRepeat: RepeatOptions, isPlayList: Boolean)

    fun getConfig(): MutableMap<String, Any>
}