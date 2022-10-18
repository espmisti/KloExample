package com.klo.example.domain.usecase

import com.klo.example.domain.model.ReadyData
import com.klo.example.domain.repository.ReferrerRepository

class GetReferrerUseCase(private val repository: ReferrerRepository) {
    suspend fun execute() : ReadyData? {
        val data = repository.getData()
        if(data != null) {
            return if(!data.contains("utm_source") && !data.contains("utm_medium") && data.contains("&c=")){
                if(data.substringAfter("&c=").contains("&")){
                    ReadyData(
                        flowkey = data.substringAfter("&c=").substringBefore("_"),
                        campaign = data.substringAfter("&c=").substringAfter("_").substringBefore("&"),
                        typeJoin = "referrer"
                    )
                } else {
                    ReadyData(
                        flowkey = data.substringAfter("&c=").substringBefore("_"),
                        campaign = data.substringAfter("&c=").substringAfter("_"),
                        typeJoin = "referrer"
                    )
                }
            } else null
        } else return null
    }
}