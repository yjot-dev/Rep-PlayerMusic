package com.yjotdev.playermusic.domain.usecase

import javax.inject.Inject
import javax.inject.Singleton
import com.yjotdev.playermusic.domain.port.ConfigRepository

@Singleton
class GetConfigUseCase @Inject constructor(
    private val configRepository: ConfigRepository
) {
    operator fun invoke() = configRepository.getConfig()
}