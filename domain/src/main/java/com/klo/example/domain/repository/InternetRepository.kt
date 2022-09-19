package com.klo.example.domain.repository

interface InternetRepository {
    suspend fun getConnection() : Boolean
}