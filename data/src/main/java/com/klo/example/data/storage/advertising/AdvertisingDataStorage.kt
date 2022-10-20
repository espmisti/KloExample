package com.klo.example.data.storage.advertising

import android.content.Context
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.klo.example.data.storage.AdvertisingStorage
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AdvertisingDataStorage(private val context: Context) : AdvertisingStorage{
    override fun getADID(): String {
        val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
        return adInfo.id.toString()
    }

    override suspend fun getAFID(): String = suspendCoroutine {
        val afInfo = AppsFlyerLib.getInstance().getAppsFlyerUID(context)
        it.resume(afInfo.toString())
    }

}