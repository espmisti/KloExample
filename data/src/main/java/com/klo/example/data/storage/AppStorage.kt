package com.klo.example.data.storage

import com.klo.example.domain.model.AppData
import com.klo.example.domain.model.FlowModel
import org.json.JSONObject

interface AppStorage {
    suspend fun getData() : AppData?
    suspend fun getFlow(flowkey: String, jsonObject: JSONObject) : FlowModel?
}