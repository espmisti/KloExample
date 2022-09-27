package com.wisdomegypt.appqd.domain.repository

interface OrganicRepository {
    suspend fun getOrganic() : String?
}