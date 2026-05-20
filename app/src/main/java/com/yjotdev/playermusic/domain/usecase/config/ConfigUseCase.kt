package com.yjotdev.playermusic.domain.usecase.config

import com.yjotdev.playermusic.domain.utils.RepeatOptions
import com.yjotdev.playermusic.domain.repository.ConfigRepository
import javax.inject.Inject

class ConfigUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    /** Obtiene las configuraciones del usuario **/
    operator fun invoke() = configRepository.getConfig()

    /** Guarda las configuraciones del usuario **/
    operator fun invoke(
        valueRepeat: RepeatOptions, isPlayList: Boolean
    ){
        configRepository.saveConfig(valueRepeat, isPlayList)
    }
}