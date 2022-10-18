package com.klo.example.domain.usecase

import com.klo.example.data.repository.FileRepository

class GetFileNameUseCase(private val repository: FileRepository) {
    suspend fun execute() = repository.getFileName()
}