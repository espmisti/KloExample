package com.klo.example.domain.repository

import com.klo.example.domain.model.URLOfferModel

interface OfferRepository {
    suspend fun getData() : URLOfferModel?
}