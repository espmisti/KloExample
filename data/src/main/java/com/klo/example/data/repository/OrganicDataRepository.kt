package com.klo.example.data.repository

import android.content.Context
import android.telephony.TelephonyManager
import com.klo.example.data.APIService
import com.klo.example.domain.model.OrganicModel
import com.klo.example.domain.repository.OrganicRepository

class OrganicDataRepository(private val pkg: String, private val tm: TelephonyManager) : OrganicRepository {
    override suspend fun getOrganic(): OrganicModel {
        val response = APIService.retrofit.getOrganic(
            pkg = pkg,
            geo = tm.networkCountryIso
        )
        if(response.isSuccessful) {
            val result = response.body()
            return OrganicModel(url = result!!.url)
        } else {
            return OrganicModel()
        }
    }
}