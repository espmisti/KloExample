package com.klo.example.domain.repository

interface RefRepository {
    suspend fun getData() : HashMap<String, String>?
}