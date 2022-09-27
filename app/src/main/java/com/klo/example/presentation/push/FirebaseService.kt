package com.klo.example.presentation.push

import android.util.Log
import androidx.core.app.NotificationCompat

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.klo.example.obfuscation.Controller
import com.klo.example.presentation.push.model.PushModel
import org.json.JSONObject

class FirebaseService : FirebaseMessagingService(){
    override fun onMessageReceived(msg: RemoteMessage) {
        super.onMessageReceived(msg)
        if(Controller().obf()) Log.i("APP_CHECK", "[PUSH] FCM Message: ${msg.data}")
        when(msg.data["type"].toString()) {
            "push" -> {
                if(Controller().obf()) {
                    val data = JSONObject(msg.data["push"].toString())
                    val builder = NotificationCompat.Builder(this, "1")
                    val model = PushModel(
                        title = data.getString("title"),
                        body = data.getString("body"),
                        headerTitle = data.getString("header_title"),
                        image = data.getString("image"),
                        smallImage = data.getString("small_image"),
                        iconImage = data.getString("icon_image")
                    )
                    PushNotification(applicationContext).startNotification(builder, model)
                }
            }
        }
    }
}