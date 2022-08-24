package com.klo.example.domain.usecase

import com.klo.example.domain.model.URLOfferModel
import com.klo.example.domain.repository.OfferRepository

class GetOfferUseCase(private val offerRepository: OfferRepository) {
    suspend fun execute() : URLOfferModel? {
        val data = offerRepository.getData()
        if (data != null) return URLOfferModel(url = data.url)
        else return null
    }
}