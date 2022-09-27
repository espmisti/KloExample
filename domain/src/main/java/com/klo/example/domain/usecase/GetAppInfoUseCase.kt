package com.klo.example.domain.usecase

import com.klo.example.domain.model.AppDataModel
import com.klo.example.domain.repository.AppRepository

class GetAppInfoUseCase(private val appRepository: AppRepository) {
    suspend fun execute() : AppDataModel? {
        val data = appRepository.getData()
        return if (data != null) {
            AppDataModel(
                appsflyer = data.appsflyer,
                fb_app_id = data.fb_app_id,
                fb_client_token = data.fb_client_token,
                error = data.error
            )
        } else null
    }
}