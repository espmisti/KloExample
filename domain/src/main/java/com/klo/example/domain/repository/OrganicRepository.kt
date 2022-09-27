package com.klo.example.domain.repository

interface OrganicRepository {
    suspend fun getOrganic() : String?
}