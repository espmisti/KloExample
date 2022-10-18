package com.klo.example.domain.usecase

import com.klo.example.domain.repository.OrganicRepository

class GetOrganicUseCase(private val repository: OrganicRepository) {
    suspend fun execute() : String? {
        return repository.getData()?.url
    }
}