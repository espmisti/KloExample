package com.klo.example.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.klo.example.domain.model.FacebookModel
import com.klo.example.domain.repository.FacebookRepository
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FacebookDataRepository (private val context: Context, private val intent: Intent) : FacebookRepository {
    override suspend fun getCampaign(): FacebookModel {
        return FacebookModel(
            campaign = getData()
        )
    }
    override suspend fun getInitialData(id: String, token: String): Boolean {
        FacebookSdk.setApplicationId(id)
        FacebookSdk.setClientToken(token)
        FacebookSdk.sdkInitialize(context.applicationContext)
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        return FacebookSdk.isInitialized()
    }
    private suspend fun getData() : String? = suspendCoroutine {
        AppLinkData.fetchDeferredAppLinkData(context.applicationContext, AppLinkData.CompletionHandler { appLink ->
            val data: Uri? = intent.data
            when {
                data?.host != null -> it.resume(data.host)
                appLink?.targetUri != null -> it.resume((appLink.targetUri).toString().substringAfter("//"))
                else -> it.resume(null)
            }
        })
    }
}