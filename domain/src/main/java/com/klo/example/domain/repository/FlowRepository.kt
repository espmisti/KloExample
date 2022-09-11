package com.klo.example.domain.repository

import com.klo.example.domain.model.FlowModel

interface FlowRepository {
    suspend fun getData() : FlowModel?
}