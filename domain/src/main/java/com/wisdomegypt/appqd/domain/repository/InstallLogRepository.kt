package com.wisdomegypt.appqd.domain.repository

interface InstallLogRepository {
    suspend fun sendInstalLog()
}