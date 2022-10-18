package com.klo.example.domain.repository

interface PushTokenRepository {
    suspend fun getData() : String?
}