package com.klo.example.domain.usecase

import com.klo.example.domain.repository.PushTokenRepository

class GetPushTokenUseCase(private val repository: PushTokenRepository) {
    suspend fun execute() = repository.getData()
}