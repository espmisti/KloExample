package com.klo.example.domain.repository

import com.klo.example.domain.model.OrganicModel

interface OrganicRepository {
    suspend fun getData() : OrganicModel?
}