package com.klo.example.app

import android.app.Application
import com.klo.example.di.AppComponent
import com.klo.example.di.AppModule
import com.klo.example.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent : AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(context = this))
            .build()
    }
}