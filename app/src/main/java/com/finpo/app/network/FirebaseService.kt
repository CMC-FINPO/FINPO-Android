package com.finpo.app.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.finpo.app.R
import com.finpo.app.ui.MainActivity
import com.finpo.app.ui.splash.SplashActivity
import com.finpo.app.utils.FCM_TYPE.CHILDCOMMENT
import com.finpo.app.utils.FCM_TYPE.COMMENT
import com.finpo.app.utils.FCM_TYPE.POLICY
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseService : FirebaseMessagingService() {
    private lateinit var notiTitle: String
    private lateinit var notiBody: String
    private var startId = R.id.homeFragment
    private var bulletinId: Int = -1

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        when(message.data["type"].toString()) {
            POLICY -> {
                val category = message.data["category"].toString()
                val region = message.data["region"].toString()
                val title = message.data["title"].toString()

                notiTitle = "$region/$category"
                notiBody = "새로 올라온 $title 정책을 확인해보시고 혜택 받아보세요!"
                startId = R.id.policyDetailFragment
                bulletinId = message.data["id"]?.toInt() ?: -1
            }
            COMMENT -> { }
            CHILDCOMMENT -> { }
        }

        createNotificationChannel()
        sendNotification(notiTitle, notiBody)
    }

    private fun createNotificationChannel() {
        val name = "name"
        val descriptionText = "description"
        val importance = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            NotificationManager.IMPORTANCE_HIGH
        } else {
            NotificationCompat.PRIORITY_HIGH
        }
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel("channel", name, importance).apply {
                description = descriptionText
            }
        } else null

        if(channel == null) return

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            notificationManager.createNotificationChannel(channel)

    }

    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("startId", startId)
        intent.putExtra("bulletinId", bulletinId)
        val pendingIntent = PendingIntent.getActivity(this,  System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this, "channel")
        } else NotificationCompat.Builder(this)

        builder
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setFullScreenIntent(pendingIntent, true)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(bulletinId, builder.build())
        }
    }
}