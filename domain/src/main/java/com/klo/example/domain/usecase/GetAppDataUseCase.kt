package com.klo.example.domain.usecase

import com.klo.example.domain.model.AppData
import com.klo.example.domain.repository.AppRepository

class GetAppDataUseCase(private val repository: AppRepository) {
    suspend fun execute() : AppData? {
        val data = repository.getData()
        return if (data != null) {
            AppData(
                fb_app_id = data.fb_app_id,
                fb_client_token = data.fb_client_token
            )
        } else null
    }
}