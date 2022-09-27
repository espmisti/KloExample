package com.wisdomegypt.appqd.domain.repository

interface PushTokenRepository {
    suspend fun getToken() : String
}