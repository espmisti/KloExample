package com.klo.example.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klo.example.data.repository.*
import com.klo.example.domain.model.*
import com.klo.example.domain.usecase.*
import com.klo.example.obfuscation.Controller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val getAppDataUseCase: GetAppDataUseCase,
    private val getFacebookUseCase: GetFacebookUseCase,
    private val getFacebookStatusUseCase: GetFacebookStatusUseCase,
    private val getReferrerUseCase: GetReferrerUseCase,
    private val getOrganicUseCase: GetOrganicUseCase,
    private val getFlowUseCase: GetFlowUseCase,
    private val getAdvertisingDataUseCase: GetAdvertisingDataUseCase
) : ViewModel() {

    // AppInfo
    private val mutableAppInfoSuccessLiveData = MutableLiveData<AppData>()
    private val mutableAppInfoFailureLiveData = MutableLiveData<String>()
    val appSuccessLiveData: LiveData<AppData> = mutableAppInfoSuccessLiveData
    val appFailureLiveData: LiveData<String> = mutableAppInfoFailureLiveData

    // Initial Facebook
    private val mutableInitFacebookSuccessLiveData = MutableLiveData<Boolean>()
    private val mutableInitFacebookFailureLiveData = MutableLiveData<String>()
    val initFacebookSuccessLiveData: LiveData<Boolean> = mutableInitFacebookSuccessLiveData
    val initFacebookFailureLiveData: LiveData<String> = mutableInitFacebookFailureLiveData

    // Получение Facebook
    private val mutableFacebookSuccessLiveData = MutableLiveData<ReadyData>()
    private val mutableFacebookFailureLiveData = MutableLiveData<String>()
    val facebookSuccessLiveData: LiveData<ReadyData> = mutableFacebookSuccessLiveData
    val facebookFailureLiveData: LiveData<String> = mutableFacebookFailureLiveData

    // Получение Referrer
    private val mutableReferrerSuccessLiveData = MutableLiveData<ReadyData>()
    private val mutableReferrerFailureLiveData = MutableLiveData<String>()
    val referrerSuccessLiveData: LiveData<ReadyData> = mutableReferrerSuccessLiveData
    val referrerFailureLiveData: LiveData<String> = mutableReferrerFailureLiveData

    // Push Token
    private val mutablePushTokenSuccessLiveData = MutableLiveData<String>()
    private val mutablePushTokenFailureLiveData = MutableLiveData<String>()
    val pushTokenSuccessLiveData: LiveData<String> = mutablePushTokenSuccessLiveData
    val pushTokenFailureLiveData: LiveData<String> = mutablePushTokenFailureLiveData

    // Получение Flow данных
    private val mutableFlowSuccessLiveData = MutableLiveData<FlowModel>()
    private val mutableFlowFailureLiveData = MutableLiveData<String>()
    val flowSuccessLiveData: LiveData<FlowModel> = mutableFlowSuccessLiveData
    val flowFailureLiveData: LiveData<String> = mutableFlowFailureLiveData

    // Получение данных рекламы
    private val mutableAdvSuccessLiveData = MutableLiveData<AdvertData>()
    private val mutableAdvFailureLiveData = MutableLiveData<String>()
    val advSuccessLiveData: LiveData<AdvertData> = mutableAdvSuccessLiveData
    val advFailureLiveData: LiveData<String> = mutableAdvFailureLiveData

    // Открытие WebView
    val mutableWebViewLiveData : MutableLiveData<HashMap<String, String>> = MutableLiveData()

    fun getAppData() = viewModelScope.launch (Dispatchers.IO) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = getAppDataUseCase.execute()
            withContext(Dispatchers.Main) {
                data?.let {
                    if(Controller().obf()) mutableAppInfoSuccessLiveData.postValue(AppData(
                        fb_app_id = data.fb_app_id,
                        fb_client_token = data.fb_client_token
                    ))
                } ?: run {
                    if(Controller().obf()) mutableAppInfoFailureLiveData.postValue("[AppInfo]: Ошибка получения данных приложения с сервера")
                }
            }
        }
    }
    fun initialFacebook(id: String, token: String) {
        val data = getFacebookStatusUseCase.execute(id = id, token = token)
        if(data) mutableInitFacebookSuccessLiveData.postValue(data)
        else mutableInitFacebookFailureLiveData.postValue("[Facebook]: Ошибка инициализации Facebook SDK")
    }
    fun getFacebook() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = getFacebookUseCase.execute()
            withContext(Dispatchers.Main) {
                data?.let {
                    if(Controller().obf()) mutableFacebookSuccessLiveData.postValue(it)
                } ?: run {
                    if(Controller().obf()) mutableFacebookFailureLiveData.postValue("[Facebook]: Кампании нету")
                }
            }
        }
    }
    fun getReferrer() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = getReferrerUseCase.execute()
            withContext(Dispatchers.Main) {
                data?.let {
                    if (Controller().obf()) mutableReferrerSuccessLiveData.postValue(it)
                } ?: run {
                    if(Controller().obf()) mutableReferrerFailureLiveData.postValue("[Referrer]: Кампания не найдена")
                }
            }
        }
    }
    fun getPushToken() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = GetPushTokenUseCase(repository = PushTokenDataRepositoryImpl()).execute()
            withContext(Dispatchers.Main) {
                data?.let {
                    if(Controller().obf()) mutablePushTokenSuccessLiveData.postValue(it)
                } ?: run {
                    if(Controller().obf()) mutablePushTokenFailureLiveData.postValue("[PUSH TOKEN]: Ошибка получения токена")
                }
            }
        }
    }
    fun getFlow(flowkey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = getFlowUseCase.execute(flowkey = flowkey)
            withContext(Dispatchers.Main) {
                data?.let {
                    if(Controller().obf()) mutableFlowSuccessLiveData.postValue(FlowModel(url = it.url, fullscreen = it.fullscreen, orientation = it.orientation))
                } ?: run {
                    if(Controller().obf()) mutableFlowFailureLiveData.postValue("[FlowKey]: Проблема с получением flowkey")
                }
            }
        }
    }
    fun getAdv() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = getAdvertisingDataUseCase.execute()
            withContext(Dispatchers.Main) {
                if(data.af_id != null && data.ad_id != null){
                    if(Controller().obf()) mutableAdvSuccessLiveData.postValue(data)
                } else {
                    if(Controller().obf()) mutableAdvFailureLiveData.postValue("[ADV]: Данные не получили")
                }
            }
        }
    }
    fun openWebView(type: Int = 0, url: String = "", fullscreen: Int = 0, orientation: Int = 0) {
        val map = hashMapOf<String, String>()
        when (type) {
            0-> {
                if(Controller().obf()) openWhite(map, fullscreen, orientation)
            }
            1-> {
                if(Controller().obf()) openURL(map, url, fullscreen, orientation)
            }
            2-> {
                viewModelScope.launch(Dispatchers.IO) {
                    val data = getOrganicUseCase.execute()
                    withContext(Dispatchers.Main) {
                        data?.let {
                            if(Controller().obf()) openURL(map, data, fullscreen, orientation)
                        } ?: run {
                            if(Controller().obf()) openWhite(map, fullscreen, orientation)
                        }
                    }
                }
            }
        }
    }
    private fun openWhite(map: HashMap<String, String>, fullscreen: Int, orientation: Int) {
        if(Controller().obf()) map["type_join"] = "organic"
        if(Controller().obf()) map["url"] = ""
        if(Controller().obf()) map["fullscreen"] = fullscreen.toString()
        if(Controller().obf()) map["orientation"] = orientation.toString()
        if(Controller().obf()) mutableWebViewLiveData.value = map
    }
    private fun openURL(map: HashMap<String, String>, url: String, fullscreen: Int, orientation: Int) {
        if(Controller().obf()) map["type_join"] = "non-organic"
        if(Controller().obf()) map["url"] = url
        if(Controller().obf()) map["fullscreen"] = fullscreen.toString()
        if(Controller().obf()) map["orientation"] = orientation.toString()
        if(Controller().obf()) mutableWebViewLiveData.value = map
    }
}