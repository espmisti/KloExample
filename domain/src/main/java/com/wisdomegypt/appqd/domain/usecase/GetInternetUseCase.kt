package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.repository.InternetRepository

class GetInternetUseCase(private val internetRepository: InternetRepository) {
    fun execute() = internetRepository.getConnection()
}