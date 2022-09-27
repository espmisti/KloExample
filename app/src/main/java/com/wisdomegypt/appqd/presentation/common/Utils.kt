package com.wisdomegypt.appqd.presentation.common

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.wisdomegypt.appqd.R
import com.wisdomegypt.appqd.data.repository.InternetDataRepository
import com.wisdomegypt.appqd.data.repository.SharedPrefDataRepository
import com.wisdomegypt.appqd.domain.model.SharedPrefModel
import com.wisdomegypt.appqd.domain.usecase.GetInternetUseCase
import com.wisdomegypt.appqd.domain.usecase.GetSharedPrefUseCase

class Utils {
    // Проверка интернета
    fun isInternetEnabled(context: Context, tag: String) : Boolean {
        val result = GetInternetUseCase(internetRepository = InternetDataRepository(context = context)).execute()
        if (tag != "nope") Log.i(tag, "[Internet]: ${if (result) "Интернет соединение есть" else "Нету интернет соединения"}")
        return result
    }

    // Создание full screen экрана
    fun setFull(win: Window, isOrganic: Boolean = true) {
        if (isOrganic){
            win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) win.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val decorView: View = win.decorView
        val uiOptions: Int = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
    }

    // Создание toolbar и navigation bar цветными
    fun setColorScreen(win: Window, context: Context) {
        win.navigationBarColor = context.resources.getColor(R.color.navigation)
        win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        win.statusBarColor = context.resources.getColor(R.color.header)
    }

    // Получение shared preference данных
    fun getSharedPrefs(context: Context) = GetSharedPrefUseCase(sharedPrefRepository = SharedPrefDataRepository(context = context)).execute()
}