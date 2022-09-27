package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.repository.RefRepository

class GetReferrerUseCase(private val referrerRepository: RefRepository) {
    suspend fun execute() = referrerRepository.getData()
}