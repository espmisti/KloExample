package com.klo.example.data.repository

import android.content.Context
import com.appsflyer.AppsFlyerLib
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.klo.example.domain.model.AdvertData
import com.klo.example.domain.repository.AdvertisingRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AdvertisingRepositoryImpl(private val context: Context) : AdvertisingRepository{
    override fun getData(): AdvertData {
        val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
        val af_id = AppsFlyerLib.getInstance().getAppsFlyerUID(context)
        val ad_id = adInfo.id.toString()
        return AdvertData(
            ad_id = ad_id,
            af_id = af_id
        )
    }
}