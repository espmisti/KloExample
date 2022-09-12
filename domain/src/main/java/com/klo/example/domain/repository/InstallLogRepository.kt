package com.klo.example.domain.repository

interface InstallLogRepository {
    suspend fun sendInstalLog()
}