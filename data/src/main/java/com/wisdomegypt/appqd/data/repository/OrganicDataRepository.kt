package com.wisdomegypt.appqd.data.repository

import android.telephony.TelephonyManager
import android.util.Log
import com.wisdomegypt.appqd.data.APIService
import com.wisdomegypt.appqd.domain.model.OrganicModel
import com.wisdomegypt.appqd.domain.repository.OrganicRepository

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