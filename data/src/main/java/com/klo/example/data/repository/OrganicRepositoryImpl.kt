package com.klo.example.data.repository

import android.telephony.TelephonyManager
import com.klo.example.data.APIService
import com.klo.example.domain.model.OrganicModel
import com.klo.example.domain.repository.OrganicRepository

class OrganicRepositoryImpl(private val package_name: String, private val tm: TelephonyManager) : OrganicRepository {
    override suspend fun getData(): OrganicModel? {
        val response = APIService.retrofit.getOrganic(pkg = package_name, geo = tm.networkCountryIso)
        return if(response.isSuccessful) {
            response.body()
        } else null
    }
}