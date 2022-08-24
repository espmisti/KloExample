package com.klo.example.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.attribution.AppsFlyerRequestListener
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.klo.example.domain.model.AppsflyerModel
import com.klo.example.domain.repository.AppsflyerRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AppsflyerDataRepository (private val context: Context,  private val appsflyer: String) : AppsflyerRepository {
    override suspend fun getData(): AppsflyerModel? {
        return getAppsflyer()
    }

    override suspend fun getAdvertisingId(): String {
        val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context)
        return adInfo.id.toString()
    }

    override suspend fun getAppsflyerId(): String {
        return AppsFlyerLib.getInstance().getAppsFlyerUID(context).toString()
    }

    private suspend fun getAppsflyer() : AppsflyerModel = suspendCoroutine {
        val listener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionData: MutableMap<String, Any>?) {
                it.resume(AppsflyerModel(
                    campaign = conversionData?.get("campaign").toString(),
                    campaign_id = conversionData?.get("campaign_id").toString(),
                    redirect_response_data = conversionData?.get("redirect_response_data").toString(),
                    adgroup_id = conversionData?.get("adgroup_id").toString(),
                    engmnt_source = conversionData?.get("engmnt_source").toString(),
                    retargeting_conversion_type = conversionData?.get("retargeting_conversion_type").toString(),
                    is_incentivized = conversionData?.get("is_incentivized").toString(),
                    orig_cost = conversionData?.get("orig_cost").toString(),
                    is_first_launch = conversionData?.get("is_first_launch").toString(),
                    af_click_lookback = conversionData?.get("af_click_lookback").toString(),
                    af_cpi = conversionData?.get("af_cpi").toString(),
                    iscache = conversionData?.get("iscache").toString(),
                    click_time = conversionData?.get("click_time").toString(),
                    is_branded_link = conversionData?.get("is_branded_link").toString(),
                    match_type = conversionData?.get("match_type").toString(),
                    adset = conversionData?.get("adset").toString(),
                    install_time = conversionData?.get("install_time").toString(),
                    media_source = conversionData?.get("media_source").toString(),
                    af_sub1 = conversionData?.get("af_sub1").toString(),
                    clickid = conversionData?.get("clickid").toString(),
                    af_siteid = conversionData?.get("af_siteid").toString(),
                    af_status = conversionData?.get("af_status").toString(),
                    cost_cents_USD = conversionData?.get("cost_cents_USD").toString(),
                    af_r = conversionData?.get("af_r").toString(),
                    af_sub4 = conversionData?.get("af_sub4").toString(),
                    af_sub3 = conversionData?.get("af_sub3").toString(),
                    af_sub2 = conversionData?.get("af_sub2").toString(),
                    adset_id = conversionData?.get("adset_id").toString(),
                    http_referrer = conversionData?.get("http_referrer").toString(),
                    is_universal_link = conversionData?.get("is_universal_link").toString(),
                    is_retargeting = conversionData?.get("is_retargeting").toString(),
                    adgroup = conversionData?.get("adgroup").toString(),
                    ts = conversionData?.get("ts").toString(),
                ))
            }
            override fun onAppOpenAttribution(attributionData: MutableMap<String, String>?) {
                Log.i("APP_CHECK", "onAppOpenAttribution: $attributionData")
            }
            override fun onConversionDataFail(p0: String?) { it.resume(AppsflyerModel("onConversionDataFail", "onConversionDataFail")) }
            override fun onAttributionFailure(attributionData: String?) { it.resume(AppsflyerModel("onAttributionFailure", "onAttributionFailure")) }
        }
        AppsFlyerLib.getInstance().registerConversionListener(context.applicationContext, listener)
    }
}