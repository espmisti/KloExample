package com.klo.example.domain.repository

import com.klo.example.domain.model.AppData
import com.klo.example.domain.model.FlowModel

interface AppRepository {
    suspend fun getData() : AppData?
    suspend fun getFlow(flowkey: String, jsonObject: JSON) : FlowModel?
}