package com.yjotdev.playermusic.domain.repository

import com.yjotdev.playermusic.domain.utils.RepeatOptions

interface ConfigRepository {

    fun saveConfig(valueRepeat: RepeatOptions, isPlayList: Boolean)

    fun getConfig(): MutableMap<String, Any>
}