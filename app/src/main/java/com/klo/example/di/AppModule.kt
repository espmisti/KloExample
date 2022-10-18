package com.klo.example.di

import android.content.Context
import com.klo.example.domain.usecase.*
import com.klo.example.presentation.splash.SplashViewModelFactory
import dagger.Module
import dagger.Provides
import org.json.JSONObject

@Module
class AppModule(val context: Context, val jsonObject: JSONObject) {
    @Provides
    fun provideContext() : Context {
        return context
    }
    @Provides
    fun provideJSONObject() : JSONObject {
        return jsonObject
    }
    @Provides
    fun provideSplashViewModelFactory(
        getAppDataUseCase: GetAppDataUseCase,
        getFlowUseCase: GetFlowUseCase,
        getAdvertisingDataUseCase: GetAdvertisingDataUseCase,
        getFacebookStatusUseCase: GetFacebookStatusUseCase,
        getFacebookUseCase: GetFacebookUseCase,
        getReferrerUseCase: GetReferrerUseCase,
        getOrganicUseCase: GetOrganicUseCase
    ) : SplashViewModelFactory {
        return SplashViewModelFactory(
            getAppDataUseCase = getAppDataUseCase,
            getFacebookUseCase = getFacebookUseCase,
            getFacebookStatusUseCase = getFacebookStatusUseCase,
            getReferrerUseCase = getReferrerUseCase,
            getOrganicUseCase = getOrganicUseCase,
            getFlowUseCase = getFlowUseCase,
            getAdvertisingDataUseCase = getAdvertisingDataUseCase)
    }
}