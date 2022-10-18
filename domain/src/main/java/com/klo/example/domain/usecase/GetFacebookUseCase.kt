package com.klo.example.domain.usecase

import com.klo.example.domain.model.ReadyData
import com.klo.example.domain.repository.FacebookRepository

class GetFacebookUseCase (private val repository: FacebookRepository) {
    suspend fun execute() : ReadyData? {
        val data = repository.getData()
        return if(data != null && data != "null") {
            if(data.contains("?")) ReadyData(data.substringBefore("_"), data.substringAfter("_").substringBefore("?"), "facebook")
            else ReadyData(data.substringBefore("_"), data.substringAfter("_"), "facebook")
        } else null
    }
}