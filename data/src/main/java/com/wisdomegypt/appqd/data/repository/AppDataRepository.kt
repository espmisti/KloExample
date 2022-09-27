package com.wisdomegypt.appqd.data.repository

import com.wisdomegypt.appqd.data.APIService
import com.wisdomegypt.appqd.domain.model.AppDataModel
import com.wisdomegypt.appqd.domain.repository.AppRepository

class AppDataRepository(private val pkg: String) : AppRepository {
    override suspend fun getData(): AppDataModel? {
        val response = APIService.retrofit.getAppInfo(pkg = pkg)
        return if(response.isSuccessful) {
            val result = response.body()
            if (result != null) {
                AppDataModel(
                    fb_client_token = result.fb_client_token,
                    fb_app_id = result.fb_app_id,
                    appsflyer = result.appsflyer
                )
            } else null
        } else {
            val result = response.body()
            return if (result != null) AppDataModel(error = result.error)
            else null
        }
    }
}