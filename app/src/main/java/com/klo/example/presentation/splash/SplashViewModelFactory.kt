package com.klo.example.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.klo.example.data.repository.*
import com.klo.example.domain.usecase.*

class SplashViewModelFactory(
    val getAppDataUseCase: GetAppDataUseCase,
    val getFlowUseCase: GetFlowUseCase,
    val getAdvertisingDataUseCase: GetAdvertisingDataUseCase,
    val getFacebookStatusUseCase: GetFacebookStatusUseCase,
    val getFacebookUseCase: GetFacebookUseCase,
    val getReferrerUseCase: GetReferrerUseCase,
    val getOrganicUseCase: GetOrganicUseCase
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SplashViewModel(
            getAppDataUseCase = getAppDataUseCase,
            getFacebookUseCase = getFacebookUseCase,
            getFacebookStatusUseCase = getFacebookStatusUseCase,
            getReferrerUseCase = getReferrerUseCase,
            getOrganicUseCase = getOrganicUseCase,
            getFlowUseCase = getFlowUseCase,
            getAdvertisingDataUseCase = getAdvertisingDataUseCase
        ) as T
    }
}