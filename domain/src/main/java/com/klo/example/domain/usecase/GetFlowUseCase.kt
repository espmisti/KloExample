package com.klo.example.domain.usecase

import com.klo.example.domain.model.FlowModel
import com.klo.example.domain.repository.FlowRepository

class GetFlowUseCase(private val flowRepository: FlowRepository) {
    suspend fun execute() : FlowModel? {
        val data = flowRepository.getData()
        if (data != null) return FlowModel(
            url = data.url,
            fullscreen = data.fullscreen,
            orientation = data.orientation
        )
        else return null
    }
}