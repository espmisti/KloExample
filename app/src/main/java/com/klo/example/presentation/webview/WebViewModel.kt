package com.klo.example.presentation.webview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klo.example.domain.usecase.GetSharedPrefUseCase
import com.klo.example.domain.usecase.SaveSharedPrefUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WebViewModel(
    val getSharedDataUseCase: GetSharedPrefUseCase,
    val saveSharedDataUseCase: SaveSharedPrefUseCase
    ) : ViewModel() {

    val mutableSaveSharedPrefLiveData : MutableLiveData<Boolean> = MutableLiveData()

    fun saveSharedPrefs(url: String? = null, fullscreen: Int? = null, orientation: Int? = null) = viewModelScope.launch(Dispatchers.IO) {
        val data = saveSharedDataUseCase.execute(url = url, fullscreen = fullscreen, orientation = orientation)
        withContext(Dispatchers.Main) {
            mutableSaveSharedPrefLiveData.postValue(data)
        }
    }
}