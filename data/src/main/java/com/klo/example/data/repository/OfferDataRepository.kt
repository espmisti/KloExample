package com.klo.example.data.repository

import com.klo.example.data.APIService
import com.klo.example.domain.model.URLOfferModel
import com.klo.example.domain.repository.OfferRepository
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject


class OfferDataRepository (private val jsonObject: JSONObject) : OfferRepository {
    override suspend fun getData(): URLOfferModel? {
        val requestBody : RequestBody = RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonObject.toString())
        val result = APIService.retrofit.getUrlOffer(requestBody = requestBody)
        if (result.isSuccessful) {
            val response = result.body()
            if (response != null) return URLOfferModel(url = response.url)
            else return null
        } else return null
    }
}