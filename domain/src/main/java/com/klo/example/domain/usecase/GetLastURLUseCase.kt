package com.klo.example.domain.usecase

import com.klo.example.domain.model.LastURLModel
import com.klo.example.domain.repository.LastURLRepository

class GetLastURLUseCase(private val lastURLRepository: LastURLRepository) {
    suspend fun execute() : LastURLModel {
        val data = lastURLRepository.getData()
        return LastURLModel(last_url = data.last_url)
    }
}