package com.klo.example.domain.usecase

import com.klo.example.domain.model.AdvertData
import com.klo.example.domain.repository.AdvertisingRepository

class GetAdvertisingDataUseCase(private val repository: AdvertisingRepository) {
    fun execute() : AdvertData {
        return repository.getData()
    }
}