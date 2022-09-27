package com.klo.example.data.repository

import android.telephony.TelephonyManager
import com.klo.example.data.APIService
import com.klo.example.domain.repository.OrganicRepository

class OrganicDataRepository(private val pkg: String, private val tm: TelephonyManager) : OrganicRepository {
    override suspend fun getOrganic(): String? {
        val response = APIService.retrofit.getOrganic(
            pkg = pkg,
            geo = tm.networkCountryIso
        )
        return if(response.isSuccessful) {
            val result = response.body()
            return result?.url
        } else null
    }
}