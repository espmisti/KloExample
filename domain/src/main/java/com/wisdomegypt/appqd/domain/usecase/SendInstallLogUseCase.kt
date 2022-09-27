package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.repository.InstallLogRepository

class SendInstallLogUseCase(private val installLogRepository: InstallLogRepository) {
    suspend fun execute() {
        installLogRepository.sendInstalLog()
    }
}