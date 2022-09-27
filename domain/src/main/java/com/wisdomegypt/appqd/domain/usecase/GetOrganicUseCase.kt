package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.model.OrganicModel
import com.wisdomegypt.appqd.domain.repository.OrganicRepository

class GetOrganicUseCase(private val organicRepository: OrganicRepository) {
    suspend fun execute() = organicRepository.getOrganic()
}