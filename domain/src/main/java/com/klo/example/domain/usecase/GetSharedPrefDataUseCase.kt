package com.klo.example.domain.usecase

import com.klo.example.domain.model.SharedPrefModel
import com.klo.example.domain.repository.SharedPrefRepository

class GetSharedPrefDataUseCase(private val repository: SharedPrefRepository) {
    fun execute() : SharedPrefModel {
        return SharedPrefModel(
            last_url = repository.getLastURL(),
            fullscreen = repository.getFullScreen(),
            orientation = repository.getOrientationScreen()
        )
    }
}