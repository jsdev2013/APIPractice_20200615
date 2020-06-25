package com.jisu.apipractice_20200615.utils

import java.text.SimpleDateFormat
import java.util.*

class TimeUtil {

    companion object {

        // 표시 시간을 재료로 주면 -> 상황에 맞는 String으로 변환 가능
        fun getTimeAgoFromCalendar(datetime: Calendar): String {

            // 10초 이내: 방금 전
            // 1분 이내: ?초 전
            // 한 시간 이내: ?분전
            // 12시간 이내: ? 시간 전
            // 그 이상: ?년?월?일 오전/오후 ?시?분

            // 현재 시간을 Calendar 형태로 추출
            val now = Calendar.getInstance()

            // 현재시간 - 넘어오는 시간 => 몇 ms 차이가 나는가?
            // 1시간 차이 => 1*60*60*1000 = 3600000 ms 와 같이 계산됨
            val msDiff = now.timeInMillis - datetime.timeInMillis

            if (msDiff < 10 * 1000){
                return "방금 전"
            } else if (msDiff < 1 * 60 * 1000) {
                val second = msDiff / 1000
                return "${second}초 전"
            } else if (msDiff < 1 * 60 * 60 * 1000) {
                val minute = msDiff / 1000 / 60
                return "${minute}초 전"
            } else if (msDiff < 12 * 60 * 60 * 1000) {
                val hour = msDiff / 1000 / 60 / 60
                return "${hour}초 전"
            } else {
                // ?년?월?일 ?시?분
                var sdf = SimpleDateFormat("YYYY년 M월 d일 a h시 m분")
                return sdf.format(datetime.time)
            }
        }
    }
}