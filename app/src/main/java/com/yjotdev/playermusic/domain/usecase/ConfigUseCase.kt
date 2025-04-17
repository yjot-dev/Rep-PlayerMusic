package com.yjotdev.playermusic.domain.usecase

import com.yjotdev.playermusic.application.mvvm.model.RepeatOptions
import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.port.ConfigRepository

@Singleton
class ConfigUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    /** Obtiene las configuraciones del usuario **/
    operator fun invoke() = configRepository.getConfig()

    /** Guarda las configuraciones del usuario **/
    operator fun invoke(
        valueRepeat: RepeatOptions,
        isShuffle: Boolean,
        isPlayList: Boolean,
        index0: Int,
        index1: Int,
        index2: Int
    ){
        configRepository.saveConfig(
            valueRepeat, isShuffle, isPlayList,
            index0, index1, index2
        )
    }
}