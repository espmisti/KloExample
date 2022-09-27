package com.klo.example.data.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.klo.example.domain.repository.PushTokenRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PushTokenDataRepository(private val context: Context) : PushTokenRepository {
    override suspend fun getToken(): String = suspendCoroutine {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("APP_CHECK", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            } else it.resume(task.result)
        })
    }
}