package com.klo.example.presentation.common

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.klo.example.data.repository.InternetDataRepositoryImpl
import com.klo.example.data.repository.SharedPrefDataRepositoryImpl
import com.klo.example.data.storage.sharedpref.SharedPrefDataStorage
import com.klo.example.domain.usecase.GetInternetUseCase
import com.klo.example.domain.usecase.GetSharedPrefDataUseCase
import com.klo.example.obfuscation.Controller

class Utils {
    // Проверка интернета
    fun isInternetEnabled(context: Context, tag: String) : Boolean {
        val result = GetInternetUseCase(internetRepository = InternetDataRepositoryImpl(context = context)).execute()
        if (tag != "nope" && Controller().obf()) Log.i(tag, "[Internet]: ${if (result) "Интернет соединение есть" else "Нету интернет соединения"}")
        return result
    }

    // Создание full screen экрана
    fun setFull(win: Window, isOrganic: Boolean = true) {
        if (isOrganic && Controller().obf()){
            if(Controller().obf()) win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) win.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        if(Controller().obf()) win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val decorView: View = win.decorView
        val uiOptions: Int = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if(Controller().obf()) decorView.systemUiVisibility = uiOptions
    }

    // Получение shared preference данных
    fun getSharedPrefs(context: Context) = GetSharedPrefDataUseCase(repository = SharedPrefDataRepositoryImpl(sharedPrefStorage = SharedPrefDataStorage(context = context))).execute()
}