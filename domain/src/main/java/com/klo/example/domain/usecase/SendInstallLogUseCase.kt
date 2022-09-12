package com.klo.example.domain.usecase

import com.klo.example.domain.repository.InstallLogRepository

class SendInstallLogUseCase(private val installLogRepository: InstallLogRepository) {
    suspend fun execute() {
        installLogRepository.sendInstalLog()
    }
}