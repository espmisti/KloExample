package com.klo.example.presentation.splash

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerLib
import com.klo.example.data.repository.*
import com.klo.example.domain.model.*
import com.klo.example.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    val mutableAppInfoLiveData : MutableLiveData<AppDataModel> = MutableLiveData()
    //
    val mutableGetSharedPrefLiveData : MutableLiveData<SharedPrefModel> = MutableLiveData()
    //
    val mutableFacebookLiveData : MutableLiveData<FacebookModel> = MutableLiveData()
    val mutableAppsflyerLiveData : MutableLiveData<AppsflyerModel> = MutableLiveData()
    val mutableReferrerLiveData : MutableLiveData<ReferrerModel> = MutableLiveData()
    val mutableFlowLiveData : MutableLiveData<FlowModel> = MutableLiveData()
    val mutableIsAppsInitLiveData : MutableLiveData<Boolean> = MutableLiveData()

    fun getAppInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetAppInfoUseCase(appRepository = AppDataRepository(context = getApplication())).execute()
            if(result != null){
                withContext(Dispatchers.Main){
                    mutableAppInfoLiveData.value = AppDataModel(
                        fb_app_id = result.fb_app_id,
                        fb_client_token = result.fb_client_token,
                        appsflyer = result.appsflyer
                    )
                }
            }
        }
    }

    fun getFacebook(intent: Intent, id: String, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetFacebookUseCase(facebookRepository = FacebookDataRepository(
                context = getApplication(),
                intent = intent,
                id = id,
                token = token
            )).execute()
            withContext(Dispatchers.Main) {
                mutableFacebookLiveData.value = FacebookModel(campaign = result?.campaign)
            }
        }
    }
    fun getAppsflyer() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetAppsflyerUseCase(appsflyerRepository = AppsflyerDataRepository(context = getApplication())).execute()
            AppsFlyerLib.getInstance().unregisterConversionListener()
            withContext(Dispatchers.Main) {
                mutableAppsflyerLiveData.value = AppsflyerModel(
                    campaign = result?.campaign,
                    advertising_id = result?.advertising_id,
                    appsflyer_id = result?.appsflyer_id,

                    adgroup = result?.adgroup,
                    adgroup_id = result?.adgroup_id,
                    adset = result?.adset,
                    adset_id = result?.adset_id,
                    af_cpi = result?.af_cpi,
                    af_r = result?.af_r,
                    af_click_lookback = result?.af_click_lookback,
                    af_siteid = result?.af_siteid,
                    af_status = result?.af_status,
                    af_sub1 = result?.af_sub1,
                    af_sub2 = result?.af_sub2,
                    af_sub3 = result?.af_sub3,
                    af_sub4 = result?.af_sub4,
                    install_time = result?.install_time,
                    is_branded_link = result?.is_branded_link,
                    is_first_launch = result?.is_first_launch,
                    is_incentivized = result?.is_incentivized,
                    is_retargeting = result?.is_retargeting,
                    iscache = result?.iscache,
                    is_universal_link = result?.is_universal_link,
                    campaign_id = result?.campaign_id,
                    click_time = result?.click_time,
                    clickid = result?.clickid,
                    cost_cents_USD = result?.cost_cents_USD,
                    orig_cost = result?.orig_cost,
                    engmnt_source = result?.engmnt_source,
                    http_referrer = result?.http_referrer,
                    media_source = result?.media_source,
                    redirect_response_data = result?.redirect_response_data,
                    retargeting_conversion_type = result?.retargeting_conversion_type,
                    match_type = result?.match_type,
                    ts = result?.ts
                )
            }
        }
    }
    fun getReferrer() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetReferrerUseCase(referrerRepository = RefDataRepository(context = getApplication())).execute()
            withContext(Dispatchers.Main) {
                mutableReferrerLiveData.value = ReferrerModel(
                    installVersion = result?.installVersion,
                    installReferrer = result?.installReferrer,
                    installBeginTimestampSeconds = result?.installBeginTimestampSeconds,
                    referrerClickTimestampSeconds = result?.referrerClickTimestampSeconds,
                    googlePlayInstantParam = result?.googlePlayInstantParam,
                    installBeginTimestampServerSeconds = result?.installBeginTimestampServerSeconds,
                    referrerClickTimestampServerSeconds = result?.referrerClickTimestampServerSeconds
                )
            }
        }
    }
    fun getFlow(jsonObject: JSONObject, flowkey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetFlowUseCase(flowRepository = FlowDataRepository(
                jsonObject = jsonObject,
                flowkey = flowkey
            )).execute()
            withContext(Dispatchers.Main) {
                result?.let {
                    mutableFlowLiveData.value = FlowModel(
                        url = it.url,
                        fullscreen = it.fullscreen,
                        orientation = it.orientation
                    )
                }
            }
        }
    }
    fun getSharedPref() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetSharedPrefUseCase(sharedPrefRepository = SharedPrefDataRepository(context = getApplication())).execute()
            withContext(Dispatchers.Main) {
                mutableGetSharedPrefLiveData.value = SharedPrefModel(
                    last_url = result.last_url,
                    fullscreen = result.fullscreen,
                    orientation = result.orientation
                )
            }
        }
    }

}