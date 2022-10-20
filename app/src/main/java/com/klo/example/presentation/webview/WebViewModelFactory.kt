package com.klo.example.presentation.webview

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.klo.example.data.repository.SharedPrefDataRepository
import com.klo.example.domain.usecase.GetSharedPrefUseCase
import com.klo.example.domain.usecase.SaveSharedPrefUseCase

class WebViewModelFactory(context: Context) : ViewModelProvider.Factory {
    // Shared Preference
    private val sharedPrefRepository by lazy(LazyThreadSafetyMode.NONE) {
        SharedPrefDataRepository(context = context)
    }
    private val getSharedPrefUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetSharedPrefUseCase(sharedPrefRepository = sharedPrefRepository)
    }
    private val saveSharedPrefUseCase by lazy(LazyThreadSafetyMode.NONE) {
        SaveSharedPrefUseCase(sharedPrefRepository = sharedPrefRepository)
    }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WebViewModel(
            getSharedDataUseCase = getSharedPrefUseCase,
            saveSharedDataUseCase = saveSharedPrefUseCase
        ) as T
    }
}