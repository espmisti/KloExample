package com.klo.example.data.repository

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.klo.example.domain.repository.ReferrerRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ReferrerRepositoryImpl (private val context: Context) : ReferrerRepository {
    override suspend fun getData(): String? = suspendCoroutine {
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK ->  {
                        val response = referrerClient.installReferrer
                        it.resume(response.installReferrer)
                        referrerClient.endConnection()
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> { it.resume(null) }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> { it.resume(null) }
                }
            }
            override fun onInstallReferrerServiceDisconnected() {
                it.resume(null)
            }
        })
    }
}