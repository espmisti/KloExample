package com.klo.example.domain.usecase

import com.klo.example.domain.model.AppDataModel
import com.klo.example.domain.repository.AppRepository

class GetAppInfoUseCase(private val appRepository: AppRepository) {
    suspend fun execute() : AppDataModel? {
        val data = appRepository.getData()
        if (data != null) {
            return AppDataModel(
                appsflyer = data.appsflyer,
                fb_app_id = data.fb_app_id,
                fb_client_token = data.fb_client_token
            )
        } else return null
    }
}