package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.repository.FacebookRepository

class InitialFacebookUseCase(private val repository: FacebookRepository) {
    fun execute(id: String?, token: String?) = repository.getInitialData(id = id, token = token)
}