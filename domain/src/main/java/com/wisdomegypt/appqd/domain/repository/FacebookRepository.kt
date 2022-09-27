package com.wisdomegypt.appqd.domain.repository


interface FacebookRepository {
    suspend fun getCampaign() : String?
    fun getInitialData(id: String?, token: String?) : Boolean
}