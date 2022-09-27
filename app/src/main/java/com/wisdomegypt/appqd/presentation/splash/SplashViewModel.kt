package com.wisdomegypt.appqd.presentation.splash

import android.app.Application
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wisdomegypt.appqd.data.repository.*
import com.wisdomegypt.appqd.domain.model.*
import com.wisdomegypt.appqd.domain.usecase.*
import com.wisdomegypt.appqd.obfuscation.Controller
import com.wisdomegypt.appqd.presentation.common.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    // AppInfo
    private val mutableAppInfoSuccessLiveData = MutableLiveData<AppDataModel>()
    private val mutableAppInfoFailureLiveData = MutableLiveData<String>()
    val appSuccessLiveData: LiveData<AppDataModel> = mutableAppInfoSuccessLiveData
    val appFailureLiveData: LiveData<String> = mutableAppInfoFailureLiveData

    // Initial Facebook
    private val mutableInitFacebookSuccessLiveData = MutableLiveData<Boolean>()
    private val mutableInitFacebookFailureLiveData = MutableLiveData<String>()
    val initFacebookSuccessLiveData: LiveData<Boolean> = mutableInitFacebookSuccessLiveData
    val initFacebookFailureLiveData: LiveData<String> = mutableInitFacebookFailureLiveData

    // Получение Facebook
    private val mutableFacebookSuccessLiveData = MutableLiveData<ArrayList<String>>()
    private val mutableFacebookFailureLiveData = MutableLiveData<String>()
    val facebookSuccessLiveData: LiveData<ArrayList<String>> = mutableFacebookSuccessLiveData
    val facebookFailureLiveData: LiveData<String> = mutableFacebookFailureLiveData

    // Получение Referrer
    private val mutableReferrerSuccessLiveData = MutableLiveData<HashMap<String, String>>()
    private val mutableReferrerFailureLiveData = MutableLiveData<String>()
    val referrerSuccessLiveData: LiveData<HashMap<String, String>> = mutableReferrerSuccessLiveData
    val referrerFailureLiveData: LiveData<String> = mutableReferrerFailureLiveData

    // Push Token
    private val mutablePushTokenLiveData = MutableLiveData<String>()
    val pushTokenLiveData: LiveData<String> = mutablePushTokenLiveData

    // Получение Flow данных
    private val mutableFlowSuccessLiveData = MutableLiveData<FlowModel>()
    private val mutableFlowFailureLiveData = MutableLiveData<String>()
    val flowSuccessLiveData: LiveData<FlowModel> = mutableFlowSuccessLiveData
    val flowFailureLiveData: LiveData<String> = mutableFlowFailureLiveData

    // Открытие WebView
    val mutableWebViewLiveData : MutableLiveData<HashMap<String, String>> = MutableLiveData()


    fun initialAppData() = viewModelScope.launch (Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = GetAppInfoUseCase(appRepository = AppDataRepository(pkg = getApplication<Application>().packageName)).execute()
            data?.let {
                if(data.error != null) {
                    if(Controller().obf())
                        mutableAppInfoFailureLiveData.postValue("[AppInfo] Ошибка запроса: ${data.error}")
                } else {
                    if(Controller().obf())
                        mutableAppInfoSuccessLiveData.postValue(AppDataModel(
                        appsflyer = data.appsflyer,
                        fb_app_id = data.fb_app_id,
                        fb_client_token = data.fb_client_token
                    ))
                }
            } ?: run {
                if(Controller().obf())
                    mutableAppInfoFailureLiveData.postValue("[AppInfo]: Ошибка получения данных приложения с сервера")
            }
        }
    }
    fun initialFacebook(id: String?, token: String?, intent: Intent) {
        val result = InitialFacebookUseCase(repository = FacebookDataRepository(context = getApplication(), intent = intent)).execute(id = id, token = token)
        if(result) mutableInitFacebookSuccessLiveData.postValue(result)
        else mutableInitFacebookFailureLiveData.postValue("[Facebook]: Ошибка инициализации Facebook SDK")
    }
    fun getFacebook(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetFacebookUseCase(facebookRepository = FacebookDataRepository(context = getApplication(), intent = intent)).execute()
            withContext(Dispatchers.Main) {
                result?.let {
                    mutableFacebookSuccessLiveData.postValue(it)
                } ?: run {
                    mutableFacebookFailureLiveData.postValue("[Facebook]: Кампании нету")
                }
            }
        }
    }
    fun getReferrer() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetReferrerUseCase(referrerRepository = RefDataRepository(context = getApplication())).execute()
            withContext(Dispatchers.Main) {
                result?.let {
                    if(it["installReferrer"] != null && !it["installReferrer"].toString().contains("utm_source") && !it["installReferrer"].toString().contains("utm_medium") && it["installReferrer"].toString().contains("&c="))
                        mutableReferrerSuccessLiveData.postValue(it)
                    else mutableReferrerFailureLiveData.postValue("[Referrer]: Кампания не найдена")
                } ?: run {
                    mutableReferrerFailureLiveData.postValue("[Referrer]: Ошибка с методами получения Referrer")
                }
            }
        }
    }
    fun getPushToken() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetPushTokenUseCase(tokenRepository = PushTokenDataRepository(context = getApplication())).execute()
            withContext(Dispatchers.Main) {
                mutablePushTokenLiveData.postValue(result)

            }
        }
    }
    fun getFlow(jsonObject: JSONObject, flowkey: String, tm: TelephonyManager) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetFlowUseCase(flowRepository = FlowDataRepository(jsonObject = jsonObject, flowkey = flowkey)).execute()
            Log.i("APP_CHECK" ,"getFlow: $flowkey")
            withContext(Dispatchers.Main) {
                result?.let {
                    SendInstallLogUseCase(installLogRepository = InstallLogDataRepository(pkg = getApplication<Application>().packageName, tm = tm, join_type = "non-organic")).execute()
                    mutableFlowSuccessLiveData.postValue(FlowModel(url = it.url, fullscreen = it.fullscreen, orientation = it.orientation))
                } ?: run {
                    SendInstallLogUseCase(installLogRepository = InstallLogDataRepository(pkg = getApplication<Application>().packageName, tm = tm, join_type = "organic"))
                    mutableFlowFailureLiveData.postValue("[FlowKey]: Проблема с получением flowkey")
                }
            }
        }
    }
    fun openWebView(type: Int = 0, url: String = "", fullscreen: Int = 0, orientation: Int = 0) {
        val map = hashMapOf<String, String>()
        val tm : TelephonyManager = getApplication<Application>().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                0-> openWhite(tm, map, fullscreen, orientation)
                1-> openURL(tm, map, url, fullscreen, orientation, "non-organic")
                2-> {
                    val result = GetOrganicUseCase(organicRepository = OrganicDataRepository(pkg = getApplication<Application>().packageName, tm = tm)).execute()
                    withContext(Dispatchers.Main) {
                        if(result != null) openURL(tm, map, result, fullscreen, orientation, "organic_url")
                        else openWhite(tm, map, fullscreen, orientation)
                    }
                }
            }
        }
    }
    private fun openWhite(tm: TelephonyManager, map: HashMap<String, String>, fullscreen: Int, orientation: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (Utils().isInternetEnabled(context = getApplication(), tag = "nope"))
                SendInstallLogUseCase(installLogRepository = InstallLogDataRepository(pkg = getApplication<Application>().packageName, tm = tm, join_type = "organic")).execute()
            withContext(Dispatchers.Main) {
                map["type_join"] = "organic"
                map["url"] = ""
                map["fullscreen"] = fullscreen.toString()
                map["orientation"] = orientation.toString()
                mutableWebViewLiveData.value = map
            }
        }
    }
    private fun openURL(tm: TelephonyManager, map: HashMap<String, String>, url: String, fullscreen: Int, orientation: Int, type: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (Utils().isInternetEnabled(context = getApplication(), tag = "nope"))
                SendInstallLogUseCase(installLogRepository = InstallLogDataRepository(pkg = getApplication<Application>().packageName, tm = tm, join_type = type)).execute()
            withContext(Dispatchers.Main) {
                map["type_join"] = type
                map["url"] = url
                map["fullscreen"] = fullscreen.toString()
                map["orientation"] = orientation.toString()
                mutableWebViewLiveData.value = map
            }
        }

    }
}