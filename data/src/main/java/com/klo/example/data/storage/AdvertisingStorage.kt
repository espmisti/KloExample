package com.klo.example.data.storage

interface AdvertisingStorage {
    fun getADID() : String
    suspend fun getAFID() : String
}