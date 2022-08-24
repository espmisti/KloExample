package com.klo.example.presentation.webview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.klo.example.data.repository.LastURLDataRepository
import com.klo.example.domain.model.LastURLModel
import com.klo.example.domain.usecase.GetLastURLUseCase
import com.klo.example.domain.usecase.SaveLastURLUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WebViewViewModel(application: Application) : AndroidViewModel(application) {

    val mutableGetLastURLLiveData : MutableLiveData<LastURLModel> = MutableLiveData()
    val mutableSaveLastURLLiveData : MutableLiveData<Boolean> = MutableLiveData()
    //

    fun getURLOffer() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = GetLastURLUseCase(lastURLRepository = LastURLDataRepository(context = getApplication())).execute()
            if(result != null) {
                withContext(Dispatchers.Main){
                    mutableGetLastURLLiveData.value = LastURLModel(last_url = result.last_url)
                }
            }
        }
    }

    fun saveURLOffer(url: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = SaveLastURLUseCase(lastURLRepository = LastURLDataRepository(context = getApplication())).execute(url = url)
            if(result != null) {
                withContext(Dispatchers.Main) {
                    mutableSaveLastURLLiveData.value = result
                }
            }
        }
    }

    
}