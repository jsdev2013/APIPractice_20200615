package com.jisu.apipractice_20200615.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.jisu.apipractice_20200615.R
import com.jisu.apipractice_20200615.datas.Notification
import com.jisu.apipractice_20200615.datas.Topic

class NotificationAdapter(
    val mContext:Context,
    val resId:Int,
    val mList:List<Notification>) : ArrayAdapter<Notification>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.notification_list_item, null)
        }

//        row가 절대 null 아님을 보장하면서 대입
        val row = tempRow!!

        var notiTitle = row.findViewById<TextView>(R.id.notiTitle)
        var notiMessage = row.findViewById<TextView>(R.id.notiMessage)

        val data = mList[position]

        notiTitle.text = data.title
        notiMessage.text = data.message

//        완성된 row를 리스트뷰의 재료로 리턴
        return row
    }

}