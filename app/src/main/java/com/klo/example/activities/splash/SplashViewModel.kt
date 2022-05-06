package com.klo.example.activities.splash

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.klo.example.components.appsflyer.AppsflyerUtils
import com.klo.example.components.url.DeeplinkChecker
import com.klo.example.components.url.ParamUtils
import com.klo.example.utils.Constants
import com.onesignal.OneSignal
import kotlinx.coroutines.*
import kotlin.coroutines.resume

class SplashViewModel(application: Application) : AndroidViewModel(application) {
    private var af_campaign: String? = null
    private var fb_campaign: String? = null
    private var af_adset_id: String? = null
    private var af_campaign_id : String? = null

    val finished: MutableLiveData<Boolean> = MutableLiveData()
    //
    val campaign: MutableLiveData<HashMap<Int, String>> = MutableLiveData()
    val campaign_id: MutableLiveData<String> = MutableLiveData()
    val adset_id: MutableLiveData<String> = MutableLiveData()

    fun initViewModel(){
        viewModelScope.launch {
            initialOneSignalNotification()
            getDeepLinks()
            when{
                af_campaign != null -> {
                    saveData(af_campaign!!)
                    finished.value = true
                }
                fb_campaign != null -> {
                    saveData(fb_campaign!!)
                    finished.value = true
                }
                else -> finished.value = false
            }
        }
    }
    private suspend fun saveData(_campaign: String){
        campaign.value = DeeplinkChecker().getSubs(_campaign)
        campaign_id.value = af_campaign_id
        adset_id.value = af_adset_id
    }
    private fun initialOneSignalNotification() {
        OneSignal.initWithContext(getApplication())
        OneSignal.setAppId(Constants.ONE_SIGNAL_ID)
    }
    private suspend fun getDeepLinks() = suspendCancellableCoroutine<Unit> {
        if(af_campaign != null || fb_campaign != null){
            it.resume(Unit)
            return@suspendCancellableCoroutine
        }
        val conversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionData: MutableMap<String, Any>) {
                it.resume(Unit)
                af_campaign = conversionData["campaign"].toString()
                af_adset_id = conversionData["af_adset_id"].toString()
                af_campaign_id = conversionData["af_c_id"].toString()
            }
            override fun onConversionDataFail(errorMessage: String?) {
                it.resume(Unit)
            }
            override fun onAppOpenAttribution(attributionData: MutableMap<String, String>?) {
                it.resume(Unit)
                fb_campaign = attributionData?.get("host")
            }
            override fun onAttributionFailure(errorMessage: String?) {
                it.resume(Unit)
            }
        }
        AppsFlyerLib.getInstance().init(Constants.APPSFLYER, conversionListener, getApplication())
        AppsFlyerLib.getInstance().start(getApplication())
    }
}