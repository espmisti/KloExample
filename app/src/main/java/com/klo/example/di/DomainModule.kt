package com.klo.example.di

import com.klo.example.domain.repository.*
import com.klo.example.domain.usecase.*
import dagger.Module
import dagger.Provides

@Module
class DomainModule {
    // App
    @Provides
    fun provideGetAppDataUseCase(appRepository: AppRepository) : GetAppDataUseCase {
        return GetAppDataUseCase(repository = appRepository)
    }
    // Flowkey
    @Provides
    fun provideGetFlowUseCase(appRepository: AppRepository) : GetFlowUseCase {
        return GetFlowUseCase(repository = appRepository)
    }
    // Facebook
    @Provides
    fun provideGetFacebookUseCase(facebookRepository: FacebookRepository) : GetFacebookUseCase {
        return GetFacebookUseCase(repository = facebookRepository)
    }
    @Provides
    fun provideGetFacebookStatusUseCase(facebookRepository: FacebookRepository) : GetFacebookStatusUseCase {
        return GetFacebookStatusUseCase(repository = facebookRepository)
    }
    // Referrer
    @Provides
    fun provideGetReferrerUseCase(referrerRepository: ReferrerRepository) : GetReferrerUseCase {
        return GetReferrerUseCase(repository = referrerRepository)
    }
    // Organic URL
    @Provides
    fun provideGetOrganicUseCase(organicRepository: OrganicRepository) : GetOrganicUseCase {
        return GetOrganicUseCase(repository = organicRepository)
    }
    // Advert
    @Provides
    fun provideGetAdvertisingDataUseCase(advertisingRepository: AdvertisingRepository) : GetAdvertisingDataUseCase {
        return GetAdvertisingDataUseCase(repository = advertisingRepository)
    }
}