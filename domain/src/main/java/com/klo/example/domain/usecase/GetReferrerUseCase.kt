package com.klo.example.domain.usecase

import com.klo.example.domain.repository.RefRepository

class GetReferrerUseCase(private val referrerRepository: RefRepository) {
    suspend fun execute() = referrerRepository.getData()
}