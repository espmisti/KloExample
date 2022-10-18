package com.klo.example.domain.repository


interface FacebookRepository {
    suspend fun getData() : String?
    fun getStatusFacebook(id: String, token: String) : Boolean
}