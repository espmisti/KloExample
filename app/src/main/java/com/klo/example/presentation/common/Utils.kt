package com.klo.example.presentation.common

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.klo.example.R
import com.klo.example.data.repository.InternetDataRepository
import com.klo.example.data.repository.SharedPrefDataRepository
import com.klo.example.domain.usecase.GetInternetUseCase
import com.klo.example.domain.usecase.GetSharedPrefUseCase
import com.klo.example.obfuscation.Controller

class Utils {
    // Проверка интернета
    fun isInternetEnabled(context: Context, tag: String) : Boolean {
        val result = GetInternetUseCase(internetRepository = InternetDataRepository(context = context)).execute()
        if (tag != "nope" && Controller().obf()) Log.i(tag, "[Internet]: ${if (result) "Интернет соединение есть" else "Нету интернет соединения"}")
        return result
    }
    // Включение/Выключение фулл скрина
    fun toggleFullScreen(window: Window) {
        if (window.decorView.systemUiVisibility == View.SYSTEM_UI_FLAG_VISIBLE) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    // Получение shared preference данных
    fun getSharedPrefs(context: Context) = GetSharedPrefUseCase(sharedPrefRepository = SharedPrefDataRepository(context = context)).execute()
}