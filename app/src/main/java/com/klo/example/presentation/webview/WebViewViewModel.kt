package com.klo.example.presentation.webview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.klo.example.data.repository.SharedPrefDataRepository
import com.klo.example.domain.model.SharedPrefModel
import com.klo.example.domain.usecase.GetSharedPrefUseCase
import com.klo.example.domain.usecase.SaveSharedPrefUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WebViewViewModel(application: Application) : AndroidViewModel(application) {

    val mutableSaveSharedPrefLiveData : MutableLiveData<Boolean> = MutableLiveData()
    //

    fun saveSharedPrefs(url: String? = null, fullscreen: Int? = null, orientation: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = SaveSharedPrefUseCase(sharedPrefRepository = SharedPrefDataRepository(context = getApplication())).execute(
                url = url,
                fullscreen = fullscreen,
                orientation = orientation
            )
            withContext(Dispatchers.Main) {
                mutableSaveSharedPrefLiveData.value = result
            }
        }
    }

    
}