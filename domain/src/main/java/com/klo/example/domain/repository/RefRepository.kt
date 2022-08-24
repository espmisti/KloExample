package com.klo.example.domain.repository

import com.klo.example.domain.model.ReferrerModel

interface RefRepository {
    suspend fun getData() : ReferrerModel?
}