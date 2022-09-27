package com.wisdomegypt.appqd.domain.usecase

import com.wisdomegypt.appqd.domain.model.FlowModel
import com.wisdomegypt.appqd.domain.repository.FlowRepository

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