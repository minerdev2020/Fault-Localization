package com.minerdev.faultlocalization.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.minerdev.faultlocalization.R
import com.minerdev.faultlocalization.utils.Constants
import com.minerdev.faultlocalization.view.activity.AlertDummyActivity
import com.minerdev.faultlocalization.view.activity.ServiceDummyActivity
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class NotificationService : Service() {
    private val manager: NotificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private var socket: Socket? = null

    override fun onCreate() {
        super.onCreate()
        setForegroundService()

        val builder = getWarningNotificationBuilder()

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
                Log.d(Constants.TAG, "Warning! " + it[0].toString())
                val intent = Intent(this, AlertDummyActivity::class.java).apply {
                    flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("data", it[0].toString())
                }
                val pendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                builder.setContentIntent(pendingIntent) //알림을 눌렀을때 실행할 인텐트 설정.
                manager.notify((System.currentTimeMillis() / 1000).toInt(), builder.build())
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        socket?.emit("register", Constants.USER_ID)
        return super.onStartCommand(
            intent,
            flags,
            startId
        ) // START_STICKY 또는 START_STICKY_COMPATIBILITY 과 같다
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        socket?.disconnect()
    }

    private fun getWarningNotificationBuilder(): NotificationCompat.Builder {
        val channelId = "channel1"
        val channelName = "警告"

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

        return builder.run {
            setSmallIcon(R.drawable.ic_round_warning_24)
            setContentTitle("警告")
            setContentText("发生故障")
            setAutoCancel(true) //선택시 자동으로 삭제되도록 설정.
            setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_VIBRATE)
        }
    }

    private fun setForegroundService() {
        val channelId = "mainChannel"
        val channelName = "故障警报服务"

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

        val intent = Intent(this, ServiceDummyActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        with(builder) {
            setSmallIcon(R.drawable.ic_round_settings_24)
            setContentTitle("故障警报服务")
            setContentIntent(pendingIntent) //알림을 눌렀을때 실행할 인텐트 설정.
            setShowWhen(false) // 알림 수신 시간 표시 여부
            setDefaults(Notification.DEFAULT_VIBRATE) // 무음 알림 설정
        }

        startForeground(1, builder.build())
    }
}