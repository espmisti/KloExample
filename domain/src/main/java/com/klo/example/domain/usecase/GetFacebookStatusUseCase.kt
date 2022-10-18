package com.klo.example.domain.usecase

import com.klo.example.domain.repository.FacebookRepository

class GetFacebookStatusUseCase(private val repository: FacebookRepository) {
    fun execute(id: String, token: String) = repository.getStatusFacebook(id = id, token = token)
}