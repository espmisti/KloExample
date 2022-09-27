package com.klo.example.presentation.push

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.klo.example.presentation.main.MainActivity
import com.klo.example.presentation.push.model.PushModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class PushNotification(private val context: Context) {
    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    @RequiresApi(Build.VERSION_CODES.N)
    fun startNotification(builder: NotificationCompat.Builder, model: PushModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val notification = createNotification(builder, model)
            withContext(Dispatchers.Main) {
                notificationManager.notify(999, notification)
                Log.i("APP_CHECK", "[PUSH]: Показал пуш")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("UnspecifiedImmutableFlag")
    private suspend fun createNotification(builder: NotificationCompat.Builder, model: PushModel): Notification {
        val i = Intent(context, MainActivity::class.java).putExtra("organic", false)
        val pendingActionIntent = PendingIntent.getActivity(context, 999, i, PendingIntent.FLAG_UPDATE_CURRENT)
        val iconImage = IconCompat.createWithBitmap(generateImage(img = model.iconImage))


        builder.setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
            .setContentTitle(model.title)
            .setContentText(model.body)
            .setSubText(model.headerTitle)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingActionIntent)
            .setLargeIcon(generateImage(img = model.smallImage))
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(generateImage(img = model.image)))
            .setSmallIcon(iconImage)
            .setOngoing(false)
            .setAutoCancel(true)
        return builder.build()

    }
    private suspend fun generateImage(img: String) : Bitmap = suspendCoroutine {
        Glide.with(context)
            .asBitmap()
            .load(img)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    it.resume(resource)
                }
                override fun onLoadFailed(errorDrawable: Drawable?) {
                    Log.e("APP_CHECK", "[PUSH]: error: $errorDrawable")
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}
