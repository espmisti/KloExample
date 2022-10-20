package com.klo.example.domain.repository

interface AdvertisingRepository {
    fun getADID() : String
    suspend fun getAFID() : String
}