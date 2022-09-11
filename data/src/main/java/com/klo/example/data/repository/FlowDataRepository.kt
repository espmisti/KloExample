package com.klo.example.data.repository

import com.klo.example.data.APIService
import com.klo.example.data.Constants
import com.klo.example.domain.model.FlowModel
import com.klo.example.domain.repository.FlowRepository
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject


class FlowDataRepository (private val jsonObject: JSONObject, private val flowkey: String) : FlowRepository {
    override suspend fun getData(): FlowModel? {
        val exampleJSON = JSONObject()
        with(exampleJSON) {
            put("token", Constants.API.TOKEN)
            put("flowkey", flowkey)
            put("data", jsonObject)
        }
        val requestBody : RequestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), exampleJSON.toString())
        val result = APIService.retrofit.getFlow(requestBody = requestBody)
        if (result.isSuccessful) {
            val response = result.body()
            if (response != null) return FlowModel(
                url = response.url,
                fullscreen = response.fullscreen,
                orientation = response.orientation
            )
            else return null
        } else return null
    }
}