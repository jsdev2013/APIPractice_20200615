package com.jisu.apipractice_20200615

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jisu.apipractice_20200615.adapters.NotificationAdapter
import com.jisu.apipractice_20200615.datas.Notification
import com.jisu.apipractice_20200615.datas.Topic
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_notification_list.*
import org.json.JSONObject

class NotificationListActivity : BaseActivity() {

    val mNotiList = ArrayList<Notification>()

    lateinit var mNotiAdater : NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_list)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        // Base에서 받은 알림버튼 숨기기
        notiFrameLayout.visibility = View.GONE

        mNotiAdater = NotificationAdapter(mContext, R.layout.notification_list_item, mNotiList)
        notiListView.adapter = mNotiAdater
    }

    override fun onResume() {
        super.onResume()
        getNotificationFromServer()
    }

    fun getNotificationFromServer(){

        ServerUtil.getRequestNotificationList(mContext, true, object : ServerUtil.JsonResponseHandler {
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")
                val notis = data.getJSONArray("notifications")

                // 알림 존재 여부에 따라 화면 View 변경
                if (notis.length() == 0) {
                    runOnUiThread {
                        Toast.makeText(mContext, "알림 내용이 없습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    // 새알림 목록을 담기 전에 기존 목록 날려버리기
                    mNotiList.clear()

                    for (i in 0..notis.length()-1) {
                        val noti = notis.getJSONObject(i)

                        mNotiList.add(Notification.getNotificationFromJson(noti))
                    }

                    // 알림을 받았으면, 받은 최신 알림의  id를 서버로 전파
                    // 여기까지 알림을 읽었다고 서버에 알려줌 (unread_noti_count를 0으로)
                    ServerUtil.postRequestNotification(mContext, mNotiList[0].id, null)

                    runOnUiThread {
                        mNotiAdater.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}