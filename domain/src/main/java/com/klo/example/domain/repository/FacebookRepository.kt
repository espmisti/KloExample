package com.klo.example.domain.repository


interface FacebookRepository {
    suspend fun getCampaign() : String?
    fun getInitialData(id: String?, token: String?) : Boolean
}