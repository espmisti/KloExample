package com.klo.example.domain.usecase

import com.klo.example.domain.repository.LastURLRepository

class SaveLastURLUseCase(private val lastURLRepository: LastURLRepository) {
    suspend fun execute(url: String?) : Boolean {
        val data = lastURLRepository.saveData(url = url)
        return data
    }
}