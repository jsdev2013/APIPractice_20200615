package com.jisu.apipractice_20200615

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.jisu.apipractice_20200615.adapters.TopicAdapter
import com.jisu.apipractice_20200615.adapters.TopicReplyAdapter
import com.jisu.apipractice_20200615.datas.Topic
import com.jisu.apipractice_20200615.datas.TopicReply
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    var topicReplyList = ArrayList<TopicReply>()
    lateinit var mTopicReplyAdapter : TopicReplyAdapter

    // -1? 정상적인 id는 절대 1일수 없다.
    // 만약 이 값이 계속 유지된다면 잘못된것
    var mTopicId = -1

    // 서버에서 받아온 주제 정보를 저장할 멤버변수
    lateinit var mTopic : Topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_topic_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        voteToFirstSideBtn.setOnClickListener {
            voteSideToServer(mTopic.sideList[0].id)
        }

        voteToSecondSideBtn.setOnClickListener {
            voteSideToServer(mTopic.sideList[1].id)
        }

        postReplyBtn.setOnClickListener {
            var myIntent = Intent(mContext, EditReplyActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mTopicId = intent.getIntExtra("topic_id",-1)

        if(mTopicId == -1){
             Toast.makeText(mContext, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show()
             return
        }

        // 제대로 id값을 받아온 경우 => 서버에 해당 토픽 진행상황 조회
        getTopicDetailFromServer()

    }

    // 진행상황을 받아와주는 함수 (별도 기능)
    fun getTopicDetailFromServer(){
        ServerUtil.getRequestTopicDetail(mContext, mTopicId, object:ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")
                val topic = data.getJSONObject("topic")

                val topicObj = Topic.getTopicFromJson(topic)
                mTopic = topicObj

                runOnUiThread {
                    topicTitleTxt.text = mTopic.title
                    Glide.with(mContext).load(mTopic.imageUrl).into(topicImg)

                    firstSideTxt.text = mTopic.sideList[0].title
                    secondSideTxt.text = mTopic.sideList[1].title
                    firstSideVoteCntTxt.text = "${mTopic.sideList[0].voteCount}표"
                    secondSideVoteCntTxt.text = "${mTopic.sideList[1].voteCount}표"
                    
                    // 내가 투표를 어디에 했냐에 따라 버튼 변화
                    if (mTopic.mySelectedSideIndex == -1) {
                        voteToFirstSideBtn.text = "투표하기"
                        voteToSecondSideBtn.text = "투표하기"
                    } else if (mTopic.mySelectedSideIndex == 0) {
                        voteToFirstSideBtn.text = "투표취소"
                        voteToSecondSideBtn.text = "갈아타기"
                    } else {
                        voteToFirstSideBtn.text = "갈아타기"
                        voteToSecondSideBtn.text = "투표취소"
                    }

                    mTopicReplyAdapter = TopicReplyAdapter(mContext, R.layout.topic_reply_list_item, mTopic.replyList)
                    replyListView.adapter = mTopicReplyAdapter
                }
            }
        })
    }
    
    // 진영에 투표하는 함수
    fun voteSideToServer(sideId:Int) {
        ServerUtil.postRequestTopicVote(mContext,  sideId, object:ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {
                val code = json.getInt("code")
                val message = json.getString("message")

                runOnUiThread {
                    if (code == 200) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        getTopicDetailFromServer()
                    } else {
                        Toast.makeText(mContext, "이미 의견을 등록해, 투표를 변경할 수 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
