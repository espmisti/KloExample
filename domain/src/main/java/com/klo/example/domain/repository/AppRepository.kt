package com.klo.example.domain.repository

import com.klo.example.domain.model.AppDataModel

interface AppRepository {
    suspend fun getData() : AppDataModel?
}