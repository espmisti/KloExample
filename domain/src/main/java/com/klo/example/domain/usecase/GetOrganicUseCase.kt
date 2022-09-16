package com.klo.example.domain.usecase

import com.klo.example.domain.model.OrganicModel
import com.klo.example.domain.repository.OrganicRepository

class GetOrganicUseCase(private val organicRepository: OrganicRepository) {
    suspend fun execute() : OrganicModel?{
        val data = organicRepository.getOrganic()
        return if(data != null)
            OrganicModel(url = data.url)
        else null
    }
}