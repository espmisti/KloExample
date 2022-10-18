package com.klo.example.data.repository

import com.klo.example.data.storage.AppStorage
import com.klo.example.domain.model.AppData
import com.klo.example.domain.model.FlowModel
import com.klo.example.domain.repository.AppRepository

class AppRepositoryImpl(private val appStorage: AppStorage) : AppRepository {
    override suspend fun getData(): AppData? {
        return appStorage.getData()
    }
    override suspend fun getFlow(flowkey: String): FlowModel? {
        return appStorage.getFlow(flowkey = flowkey)
    }
}