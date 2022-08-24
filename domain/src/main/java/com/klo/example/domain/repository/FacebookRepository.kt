package com.klo.example.domain.repository

import com.klo.example.domain.model.FacebookModel


interface FacebookRepository {
    suspend fun getCampaign() : FacebookModel?
}