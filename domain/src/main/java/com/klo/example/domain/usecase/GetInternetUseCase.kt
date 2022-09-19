package com.klo.example.domain.usecase

import com.klo.example.domain.repository.InternetRepository

class GetInternetUseCase(private val internetRepository: InternetRepository) {
    suspend fun execute() : Boolean {
        return internetRepository.getConnection()
    }
}