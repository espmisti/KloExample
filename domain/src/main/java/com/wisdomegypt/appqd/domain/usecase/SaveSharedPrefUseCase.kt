package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.repository.SharedPrefRepository

class SaveSharedPrefUseCase(private val sharedPrefRepository: SharedPrefRepository) {
    suspend fun execute(url: String? = null, fullscreen: Int? = null, orientation: Int? = null) : Boolean {
        if (url != null) sharedPrefRepository.saveLastURL(value = url)
        if (fullscreen != null) sharedPrefRepository.saveFullScreen(value = fullscreen)
        if (orientation != null) sharedPrefRepository.saveOrientationScreen(value = orientation)
        return true
    }
}