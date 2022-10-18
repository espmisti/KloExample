package com.klo.example.data.storage.app

import android.content.Context
import com.klo.example.data.APIService
import com.klo.example.data.Constants
import com.klo.example.data.storage.AppStorage
import com.klo.example.domain.model.AppData
import com.klo.example.domain.model.FlowModel
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class AppDataStorage(private val package_name: String, private val jsonObject: JSONObject) : AppStorage {
    override suspend fun getData(): AppData? {
        val response = APIService.retrofit.getAppInfo(pkg = package_name)
        return if(response.isSuccessful)
            response.body()
        else null
    }

    override suspend fun getFlow(flowkey: String, jsonObject: JSONObject): FlowModel? {
        val exampleJSON = JSONObject()
        with(exampleJSON) {
            put("token", Constants.API.TOKEN)
            put("flowkey", flowkey)
            put("data", jsonObject)
        }
        val requestBody : RequestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), exampleJSON.toString())
        val result = APIService.retrofit.getFlow(requestBody = requestBody)
        return if (result.isSuccessful) {
            val response = result.body()
            if (response != null) FlowModel(
                url = response.url,
                fullscreen = response.fullscreen,
                orientation = response.orientation
            )
            else null
        } else null
    }
}