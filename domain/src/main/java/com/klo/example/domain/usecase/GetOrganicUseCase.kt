package com.klo.example.domain.usecase

import com.klo.example.domain.repository.OrganicRepository

class GetOrganicUseCase(private val organicRepository: OrganicRepository) {
    suspend fun execute() = organicRepository.getOrganic()
}