package com.jisu.apipractice_20200615.datas

import org.json.JSONObject
import java.util.*

class Notification {

    var id = 0
    var receiveUserId = 0
    var actUserId = 0
    var title = ""
    var type = ""
    var message = ""
    var referenceUi = ""
    var focusObjectId = 0
    val createdAt = Calendar.getInstance()

    companion object {

        fun getNotificationFromJson(json : JSONObject) : Notification {

            val noti = Notification()

            noti.id = json.getInt("id")
            noti.receiveUserId = json.getInt("receive_user_id")
            noti.actUserId = json.getInt("act_user_id")
            noti.title = json.getString("title")
            noti.message = json.getString("message")
            noti.type = json.getString("type")
            noti.referenceUi = json.getString("reference_ui")
            noti.focusObjectId = json.getInt("focus_object_id")

            return noti
        }

    }

}