package com.klo.example.domain.usecase

import com.klo.example.domain.model.AdvertData
import com.klo.example.domain.repository.AdvertisingRepository

class GetAdvertisingDataUseCase(private val repository: AdvertisingRepository) {
    suspend fun execute() : AdvertData{
        val afId = repository.getAFID()
        val adId = repository.getADID()
        return AdvertData(af_id = afId, ad_id = adId)
    }
}