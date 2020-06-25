package com.jisu.apipractice_20200615.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService :FirebaseMessagingService() {

    // 새 토큰 발급 확인 함수
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.d("새토큰 발급", p0)
    }

    // 실제 푸시알림 수신 시 실행되는 함수
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.d("발급받은 발급", p0.toString())
    }
}