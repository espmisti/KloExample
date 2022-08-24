package com.klo.example.data.repository

import android.content.Context
import com.klo.example.data.APIService
import com.klo.example.domain.model.AppDataModel
import com.klo.example.domain.repository.AppRepository

class AppDataRepository(private val context: Context) : AppRepository {

    override suspend fun getData(): AppDataModel? {
        val response = APIService.retrofit.getAppInfo(pkg = context.packageName)
        if(response.isSuccessful) {
            val result = response.body()
            if (result != null) {
                return AppDataModel(
                    fb_client_token = result.fb_client_token,
                    fb_app_id = result.fb_app_id,
                    appsflyer = result.appsflyer
                )
            } else return null
        } else return null
    }
}