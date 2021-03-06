package com.jisu.apipractice_20200615.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class TopicReply {

    var id = 0
    var content = ""
    var topicId = 0
    var sideId = 0
    var userId = 0

    //  좋아요 / 싫어요 / 답글 갯수
    var likeCount = 0
    var dislikeCount = 0
    var replyCount = 0

    lateinit var writer: User
    
    // 의견이 어떤 진영을 옹호하는지
    lateinit var selectedSide: TopicSide

    var isMyLike = false
    var isMyDislike = false

    // 속성일시를 시간 형태로 저장 변수 => 기본값: 현재일시
    val createdAt = Calendar.getInstance()

    companion object {
        fun getTopicReplyFromJson(json: JSONObject) :TopicReply {
            var tr = TopicReply()

            tr.id = json.getInt("id")
            tr.content = json.getString("content")
            tr.topicId = json.getInt("topic_id")
            tr.sideId = json.getInt("side_id")
            tr.userId = json.getInt("user_id")

            val userObject = json.getJSONObject("user")
            tr.writer = User.getUserFromJson(userObject)

            // 의견 작성 시간 파싱 => 우선 String으로 가져와야 함
            val createdAtStr = json.getString("created_at")
            // String => tr.createdAt (Calendar)의 시간으로 반영
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            // 파싱중인 의견의 작성시간을 String으로 분석한 서버에서 알려준 작성시간으로 대입
            tr.createdAt.time = sdf.parse(createdAtStr)!!

            val myPhoneTimeZone = tr.createdAt.timeZone //  어느 지역 시간대인지 따서 저장(서울)
            val timeOffset = myPhoneTimeZone.rawOffset / 1000 / 60 / 60
            tr.createdAt.add(Calendar.HOUR, timeOffset)

            //  좋아요 / 싫어요 / 답글 갯수를 추가 파싱 => 목록 화면에 반영
            tr.likeCount = json.getInt("like_count")
            tr.dislikeCount = json.getInt("dislike_count")
            tr.replyCount = json.getInt("reply_count")

            // 선택진영 정보 파싱 => selected_side JSON => topicSide 전화
            tr.selectedSide = TopicSide.getTopicSideFromJson(json.getJSONObject("selected_side"))

            tr.isMyLike = json.getBoolean("my_like")
            tr.isMyDislike = json.getBoolean("my_dislike")

            return tr
        }
    }
}