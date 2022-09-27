package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.model.SharedPrefModel
import com.wisdomegypt.appqd.domain.repository.SharedPrefRepository

class GetSharedPrefUseCase(private val sharedPrefRepository: SharedPrefRepository) {
    fun execute() : SharedPrefModel {
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