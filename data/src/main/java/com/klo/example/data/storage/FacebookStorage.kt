package com.klo.example.data.storage

interface FacebookStorage {
    suspend fun getData() : String?
    fun getStatus(id: String, token: String) : Boolean
}