package com.yjotdev.playermusic.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.application.mvvm.model.RepeatOptions
import com.yjotdev.playermusic.domain.port.ConfigRepository

@Singleton
class SaveConfigUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
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