package com.jisu.apipractice_20200615.datas

import org.json.JSONObject

class Topic {

    var id = 0
    var title = ""
    var imageUrl = ""

    val sideList = ArrayList<TopicSide>()
    val replyList = ArrayList<TopicReply>()

    var mySideId = 0
    // 내가 선택한 진영이 첫번째인지, 두번째인지, 아니면 선택을 안했는지 기록하는 변수
    var mySelectedSideIndex = -1 // 선택안됨: -1, 그 외숫자: 발견된 위치(index)

    // 내가 선택한 진영 정보 => 투표를 안했으면 null 이어야 함
    var mySideInfo : TopicSide? = null

    companion object {
        fun getTopicFromJson(json:JSONObject):Topic {
            val topic = Topic()

            topic.id = json.getInt("id")
            topic.title = json.getString("title")
            topic.imageUrl = json.getString("img_url")
            
            // 선택 가능 진영 정보 파싱 => JSPNArray 파싱부터
            val sides = json.getJSONArray("sides")

            for (i in 0..sides.length()-1) {
                val side = sides.getJSONObject(i)

                // 어떻게 해야 주제의 하위항목이 되나?
                // 해당 주제의 진영 배열의 재료로 추가
                val topicSide = TopicSide.getTopicSideFromJson(side)
                topic.sideList.add(topicSide)
            }

            // 내가 투표한 진영의 id 값 => 투표안했으면 서버에서 -1로 내려 줌
            topic.mySideId = json.getInt("my_side_id")

            // 주제의 모든 진영중에 내 진영 id와 같은 값이 있는지 검사
            // 한번도 발견 안되면, 초기값 -1을 그대로 유지
            // 발견되는 순간 => 그 index 값을 저장
            for (i in topic.sideList.indices) {
                
                // i번째의 반복에서 내가 선택한 진영의 id와 같은 id값을 가진 진영을 찾았다.
                if(topic.sideList[i].id == topic.mySideId) {
                    // 진영들을 돌다가 => 내가 선택한 걸 찾았으면 => 그게 몇번째였는지를 기록
                    topic.mySelectedSideIndex = i
                }
            }

            // 의견목록 JSONArray 파싱
            var replies = json.getJSONArray("replies")
            for (i in 0..replies.length()-1) {
                // JSONArray의 i번째 추출
                topic.replyList.add(TopicReply.getTopicReplyFromJson(replies.getJSONObject((i))))
            }

            // 내 선택 진영 파싱 => 투표를 안했으면 null 임
            // null이 아닐 경우만 파싱
            if (!json.isNull("my_side")) {
                topic.mySideInfo = TopicSide.getTopicSideFromJson(json.getJSONObject("my_side"))
            }


            return topic
        }
    }
}