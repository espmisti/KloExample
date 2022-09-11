package com.klo.example.data.repository

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.klo.example.domain.model.ReferrerModel
import com.klo.example.domain.repository.RefRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RefDataRepository (private val context: Context) : RefRepository{
    override suspend fun getData(): ReferrerModel? {
        val referrerClient = InstallReferrerClient.newBuilder(context.applicationContext).build()
        return referrerListener(referrerClient)
    }
    private suspend fun referrerListener(referrerClient: InstallReferrerClient) : ReferrerModel? = suspendCoroutine {
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK ->  {
                        val response = referrerClient.installReferrer
                        referrerClient.endConnection()
                        it.resume(ReferrerModel(
                            installVersion = response.installVersion,
                            installReferrer = response.installReferrer,
                            installBeginTimestampSeconds = response.installBeginTimestampSeconds.toString(),
                            referrerClickTimestampSeconds = response.referrerClickTimestampSeconds.toString(),
                            googlePlayInstantParam = response.googlePlayInstantParam.toString(),
                            installBeginTimestampServerSeconds = response.installBeginTimestampServerSeconds.toString(),
                            referrerClickTimestampServerSeconds = response.referrerClickTimestampSeconds.toString()
                        ))
                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> { it.resume(null) }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> { it.resume(null) }
                }
            }
            override fun onInstallReferrerServiceDisconnected() {}
        })
    }

}