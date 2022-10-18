package com.klo.example.di

import android.content.Context
import android.telephony.TelephonyManager
import com.klo.example.data.repository.*
import com.klo.example.data.storage.AppStorage
import com.klo.example.data.storage.FacebookStorage
import com.klo.example.data.storage.app.AppDataStorage
import com.klo.example.data.storage.facebook.FacebookDataStorage
import com.klo.example.domain.repository.*
import dagger.Module
import dagger.Provides
import org.json.JSONObject

@Module
class DataModule {
    // App
    @Provides
    fun provideAppDataStorage(context: Context, jsonObject: JSONObject) : AppStorage {
        return AppDataStorage(package_name = context.packageName, jsonObject = jsonObject)
    }
    @Provides
    fun provideAppRepository(appStorage: AppStorage) : AppRepository {
        return AppRepositoryImpl(appStorage = appStorage)
    }
    // Facebook
    @Provides
    fun provideFacebookDataStorage(context: Context) : FacebookStorage {
        return FacebookDataStorage(context = context)
    }
    @Provides
    fun provideFacebookRepository(facebookStorage: FacebookStorage) : FacebookRepository {
        return FacebookRepositoryImpl(facebookStorage = facebookStorage)
    }
    // Referrer
    @Provides
    fun provideReferrerRepository(context: Context) : ReferrerRepository {
        return ReferrerRepositoryImpl(context = context)
    }
    // Organic URL
    @Provides
    fun provideOrganicRepository(context: Context) : OrganicRepository {
        return OrganicRepositoryImpl(package_name = context.packageName, tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
    }
    // Advert
    @Provides
    fun provideAdvertisingRepository(context: Context) : AdvertisingRepository {
        return AdvertisingRepositoryImpl(context = context)
    }
}