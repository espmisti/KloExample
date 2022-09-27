package com.wisdomegypt.appqd.data.repository

import com.wisdomegypt.appqd.data.APIService
import com.wisdomegypt.appqd.data.Constants
import com.wisdomegypt.appqd.domain.model.FlowModel
import com.wisdomegypt.appqd.domain.repository.FlowRepository
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