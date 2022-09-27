package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.repository.PushTokenRepository

class GetPushTokenUseCase(private val tokenRepository: PushTokenRepository) {
    suspend fun execute() = tokenRepository.getToken()
}