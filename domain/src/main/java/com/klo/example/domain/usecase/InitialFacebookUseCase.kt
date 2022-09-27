package com.klo.example.domain.usecase

import com.klo.example.domain.repository.FacebookRepository

class InitialFacebookUseCase(private val repository: FacebookRepository) {
    fun execute(id: String?, token: String?) = repository.getInitialData(id = id, token = token)
}