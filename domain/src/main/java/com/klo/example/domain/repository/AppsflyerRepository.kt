package com.klo.example.domain.repository

import com.klo.example.domain.model.AppsflyerModel

interface AppsflyerRepository {
    suspend fun getData() : AppsflyerModel?
    suspend fun getAdvertisingId() : String?
    suspend fun getAppsflyerId() : String?
}