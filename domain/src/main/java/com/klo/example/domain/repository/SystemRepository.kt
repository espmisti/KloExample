package com.klo.example.domain.repository

import com.klo.example.domain.model.SystemModel

interface SystemRepository {
    suspend fun getData() : SystemModel
}