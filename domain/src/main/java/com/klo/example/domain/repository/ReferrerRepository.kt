package com.klo.example.domain.repository

interface ReferrerRepository {
    suspend fun getData() : String?
}