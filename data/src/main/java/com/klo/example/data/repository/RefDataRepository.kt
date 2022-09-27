package com.klo.example.data.repository

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.klo.example.domain.repository.RefRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class RefDataRepository (private val context: Context) : RefRepository{
    override suspend fun getData(): HashMap<String, String>? = suspendCoroutine {
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK ->  {
                        val response = referrerClient.installReferrer
                        val map = hashMapOf<String, String>()
                        response?.let { data->
                            map["installReferrer"] = data.installReferrer
                            map["installBeginTimestampSeconds"] = data.installBeginTimestampSeconds.toString()
                            map["referrerClickTimestampSeconds"] = data.referrerClickTimestampSeconds.toString()
                            map["googlePlayInstantParam"] = data.googlePlayInstantParam.toString()
                            map["installBeginTimestampServerSeconds"] = data.installBeginTimestampServerSeconds.toString()
                            map["referrerClickTimestampServerSeconds"] = data.referrerClickTimestampServerSeconds.toString()
                            it.resume(map)
                        } ?: run {
                            it.resume(null)
                        }
                        referrerClient.endConnection()

                    }
                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> { it.resume(null) }
                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> { it.resume(null) }
                }
            }
            override fun onInstallReferrerServiceDisconnected() {}
        })
    }
}