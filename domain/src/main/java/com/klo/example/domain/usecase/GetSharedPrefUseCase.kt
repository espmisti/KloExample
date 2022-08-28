package com.klo.example.domain.usecase

import com.klo.example.domain.model.SharedPrefModel
import com.klo.example.domain.repository.SharedPrefRepository

class GetSharedPrefUseCase(private val sharedPrefRepository: SharedPrefRepository) {
    suspend fun execute() : SharedPrefModel {
        val dataOfFullScreen = sharedPrefRepository.getFullScreen()
        val dataOfLastURL = sharedPrefRepository.getLastURL()
        val dataOfOrientation = sharedPrefRepository.getOrientationScreen()
        return SharedPrefModel(
            last_url = dataOfLastURL,
            fullscreen = dataOfFullScreen,
            orientation = dataOfOrientation
        )
    }
}