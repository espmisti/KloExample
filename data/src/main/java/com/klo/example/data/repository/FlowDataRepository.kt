package com.klo.example.data.repository

import com.klo.example.data.APIService
import com.klo.example.data.Constants
import com.klo.example.domain.model.FlowModel
import com.klo.example.domain.repository.FlowRepository
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
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
        val requestBody : RequestBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), exampleJSON.toString())
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