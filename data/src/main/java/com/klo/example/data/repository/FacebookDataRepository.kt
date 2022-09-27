package com.klo.example.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.klo.example.domain.repository.FacebookRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class FacebookDataRepository (private val context: Context, private val intent: Intent) : FacebookRepository {
    override suspend fun getCampaign(): String? = suspendCoroutine {
        AppLinkData.fetchDeferredAppLinkData(context, AppLinkData.CompletionHandler { appLink ->
            val data: Uri? = intent.data
            when {
                data?.host != null -> it.resume(intent.data.toString().substringAfter("//"))
                appLink?.targetUri != null -> it.resume((appLink.targetUri).toString().substringAfter("//"))
                else -> it.resume(null)
            }
        })
    }
    override fun getInitialData(id: String?, token: String?): Boolean {
        FacebookSdk.setApplicationId(id!!)
        FacebookSdk.setClientToken(token)
        FacebookSdk.sdkInitialize(context)
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        return FacebookSdk.isInitialized()
    }
}