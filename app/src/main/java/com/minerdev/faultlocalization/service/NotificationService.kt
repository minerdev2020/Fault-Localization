package com.minerdev.faultlocalization.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.utils.Constants
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class NotificationService : Service() {
    private val manager: NotificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private var socket: Socket? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setWarningNotification()
        setForegroundService()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        socket?.disconnect()
    }

    private fun setWarningNotification() {
        val channelId = "channel1"
        val channelName = "Channel1"

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(channelId) == null) {
                manager.createNotificationChannel(
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )
            }

            builder = NotificationCompat.Builder(this, channelId)

        } else {
            builder = NotificationCompat.Builder(this)
                .setPriority(Notification.PRIORITY_HIGH)
        }

        builder.run {
            setSmallIcon(R.drawable.ic_round_warning_24)
            setContentTitle("경고")
            setContentText("뻥이야~")
            setAutoCancel(true) //선택시 자동으로 삭제되도록 설정.
            setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
//            setContentIntent(pendingIntent) //알림을 눌렀을때 실행할 인텐트 설정.
        }

        try {
            socket = IO.socket(Constants.BASE_URL)

        } catch (e: URISyntaxException) {
            Log.e(Constants.TAG, e.reason)
        }

        socket?.let { s ->
            s.connect()
            s.on(Socket.EVENT_CONNECT) {
                Log.d(Constants.TAG, "Connected!")
            }
            s.on("warning") {
                Log.d(Constants.TAG, "Warning!")
                manager.notify((System.currentTimeMillis() / 1000).toInt(), builder.build())
            }
        }
    }

    private fun setForegroundService() {
        val channelId = "mainChannel"
        val channelName = "MainChannel"

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(channelId) == null) {
                manager.createNotificationChannel(
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_LOW
                    ).apply { setShowBadge(false) }
                )
            }

            builder = NotificationCompat.Builder(this, channelId)

        } else {
            builder = NotificationCompat.Builder(this)
                .setPriority(Notification.PRIORITY_LOW)
        }

        builder.run {
            setSmallIcon(R.drawable.ic_round_settings_24)
            setContentTitle("고장 알림 서비스")
        }

        startForeground(1, builder.build())
    }
}