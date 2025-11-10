package com.yjotdev.playermusic.domain.usecase.config

import com.yjotdev.playermusic.domain.entity.RepeatOptions
import com.yjotdev.playermusic.domain.port.ConfigPort
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigUseCase @Inject constructor(
    private val configPort: ConfigPort
) {
    /** Obtiene las configuraciones del usuario **/
    operator fun invoke() = configPort.getConfig()

    /** Guarda las configuraciones del usuario **/
    operator fun invoke(
        valueRepeat: RepeatOptions, isPlayList: Boolean
    ){
        configPort.saveConfig(valueRepeat, isPlayList)
    }
}