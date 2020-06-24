package com.jisu.apipractice_20200615

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.jisu.apipractice_20200615.adapters.TopicReReplyAdapter
import com.jisu.apipractice_20200615.adapters.TopicReplyAdapter
import com.jisu.apipractice_20200615.datas.TopicReply
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

    // 어떤의견을 보러 온건지
    var mReplyId = -1

    //서버에서 받아온 의견 저장
    lateinit var mReply : TopicReply

    // 서버에서 보내주는 답글 목록을 저장할 배열
    val mReReplyList = ArrayList<TopicReply>()

    // 답글 목록 뿌리는 어댑터
   lateinit var mTopicReReplyAdapter: TopicReReplyAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reply_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        postBtn.setOnClickListener {

            val inputContent = contentEdt.text.toString()

            if(inputContent.length < 5) {
                Toast.makeText(mContext,"댓글의 길이는 5자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 서버에 실제 요청
            ServerUtil.postRequestReReplay(mContext, mReplyId, inputContent, object : ServerUtil.JsonResponseHandler {
                override fun onResponse(json: JSONObject) {
                    val code = json.getInt("code")
                    val message = json.getString("message")

                    runOnUiThread {
                        if (code == 200) {
                            // 입력한 답글의 내용도 비워두기
                            contentEdt.setText("")
                            Toast.makeText(mContext, "답글을 등록했습니다.", Toast.LENGTH_SHORT).show()

                            // 의견이 달린 답글들을 다시 불어와서 리스트뷰에 뿌려주기
                            getReplyDetailFromServer()

                            //Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(mContext, "이미 의견을 등록한 사람입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

    }

    override fun setValues() {

        // 타이틀 바 제목
        setTitle("의견 상세 보기")
        
        mReplyId = intent.getIntExtra("replyId", -1)

        mTopicReReplyAdapter = TopicReReplyAdapter(mContext, R.layout.topic_rereply_list_item, mReReplyList)
        reReplyListView.adapter = mTopicReReplyAdapter
    }

    override fun onResume() {
        super.onResume()
        getReplyDetailFromServer()
    }

    fun getReplyDetailFromServer(){

        ServerUtil.getRequestReplyDetail(mContext, mReplyId, object:ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {
                val data = json.getJSONObject("data")
                val reply = data.getJSONObject("reply")
                val replies = reply.getJSONArray("replies")

                mReply = TopicReply.getTopicReplyFromJson(reply)
                
                // mReReplyList 내부에 이미 들어있던 데이터가 중복으로 나와서 문제 발생
                mReReplyList.clear()
                
                // 이부분에서 mReReplyList을 채워넣고, 새로고침을 하자
                for (i in 0..replies.length()-1){
                    val replyObj = TopicReply.getTopicReplyFromJson(replies.getJSONObject(i))
                    mReReplyList.add(replyObj)
                }

                runOnUiThread {
                    contentTxt.text = mReply.content
                    writerNickNameTxt.text = mReply.writer.nickname
                    selectedSideTitleTxt.text = "(${mReply.selectedSide.title})"
                    mTopicReReplyAdapter.notifyDataSetChanged()

                    // 리스트뷰의 스크롤을 맨 밑 (마지막 포지션) 으로 이동
                    reReplyListView.smoothScrollToPosition(mReReplyList.size - 1)

                    // 키보드 숨기기
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(contentEdt.windowToken, 0)
                }
            }
        })
    }
}