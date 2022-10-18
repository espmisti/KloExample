package com.klo.example.data.storage.facebook

import android.content.Context
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.klo.example.data.storage.FacebookStorage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FacebookDataStorage(private val context: Context) : FacebookStorage{
    override suspend fun getData(): String? = suspendCoroutine {
        AppLinkData.fetchDeferredAppLinkData(context, AppLinkData.CompletionHandler { appLink ->
            it.resume((appLink?.targetUri.toString()))
        })
    }
    override fun getStatus(id: String, token: String) : Boolean{
        FacebookSdk.setApplicationId(id)
        FacebookSdk.setClientToken(token)
        FacebookSdk.sdkInitialize(context)
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        return FacebookSdk.isInitialized()
    }
}