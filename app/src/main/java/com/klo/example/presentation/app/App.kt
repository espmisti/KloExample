package com.klo.example.presentation.app

import android.app.Application
import com.appsflyer.AppsFlyerLib
import com.klo.example.R

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppsFlyerLib.getInstance().init(getString(R.string.af_dev_key), null, this)
    }
}