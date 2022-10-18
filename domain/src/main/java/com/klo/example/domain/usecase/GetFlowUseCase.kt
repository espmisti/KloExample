package com.klo.example.domain.usecase

import com.klo.example.domain.model.FlowModel
import com.klo.example.domain.repository.AppRepository
import com.klo.example.domain.repository.FlowRepository

class GetFlowUseCase(private val repository: AppRepository) {
    suspend fun execute(flowkey: String) : FlowModel? {
        val data = repository.getFlow(flowkey = flowkey)
        return if (data != null) FlowModel(
            url = data.url,
            fullscreen = data.fullscreen,
            orientation = data.orientation
        )
        else null
    }
}