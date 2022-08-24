package com.klo.example.domain.usecase

import com.klo.example.domain.model.ReferrerModel
import com.klo.example.domain.repository.RefRepository

class GetReferrerUseCase(private val referrerRepository: RefRepository) {
    suspend fun execute() : ReferrerModel? {
        val data = referrerRepository.getData()
        if (data != null) {
            return ReferrerModel(
                installVersion = data.installVersion,
                installReferrer = data.installReferrer,
                installBeginTimestampSeconds = data.installBeginTimestampSeconds,
                referrerClickTimestampSeconds = data.referrerClickTimestampSeconds,
                googlePlayInstantParam = data.googlePlayInstantParam,
                installBeginTimestampServerSeconds = data.installBeginTimestampServerSeconds,
                referrerClickTimestampServerSeconds = data.referrerClickTimestampServerSeconds
            )
        } else return null
    }
}