package com.klo.example.domain.usecase

import com.klo.example.domain.repository.PushTokenRepository

class GetPushTokenUseCase(private val tokenRepository: PushTokenRepository) {
    suspend fun execute() = tokenRepository.getToken()
}