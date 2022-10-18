package com.klo.example.di

import com.klo.example.presentation.main.MainActivity
import com.klo.example.presentation.splash.SplashFragment
import dagger.Component

@Component(modules = [AppModule::class, DomainModule::class, DataModule::class])
interface AppComponent {
    fun inject(splashFragment: SplashFragment)
}