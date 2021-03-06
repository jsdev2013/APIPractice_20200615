package com.jisu.apipractice_20200615

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.jisu.apipractice_20200615.utils.ContextUtil
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_edit_reply.*
import org.json.JSONObject

class EditReplyActivity : BaseActivity() {

    // 토픽 아이디
    var mTopicId = -1
    
    // 토픽에 대한 의견 아이디
    var mReplyId = -1
    var mIsNew = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_reply)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        // 의견 작성 후 버튼 클릭
        // "정말 의견을 등록하시겠습니까? 한번 의견을 등록하면 투표를 변경할 수 없고, 내용을 수정할 수 없습니다."
        // 확인 누르면, 실제로 의견 등록 처리
        // 완료 후 토론 진행 화면 복귀 => 자동 새로 고침
        postReplyBtn.setOnClickListener {

            val content = contentEdt.text.toString()

            if (content.length < 5) {
                Toast.makeText(mContext, "의견은 최소 5자는 되어야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mIsNew) { // 신규 등록
                val alert = AlertDialog.Builder(mContext)
                alert.setTitle("의견 등록")
                alert.setMessage("정말 의견을 등록하시겠습니까? 한번 의견을 등록하면 투표를 변경할 수 없습니다.")
                alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

                    // 서버에 실제 요청
                    ServerUtil.postRequestTopicReplay(mContext, mTopicId, content, object : ServerUtil.JsonResponseHandler {
                        override fun onResponse(json: JSONObject) {
                            val code = json.getInt("code")
                            val message = json.getString("message")

                            runOnUiThread {
                                if (code == 200) {
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

                                    // 토픽 상세보기로 이동
                                    val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
                                    myIntent.putExtra("topic_id",mTopicId)
                                    startActivity(myIntent)
                                } else {
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    })

                })
                alert.setNegativeButton("취소", null)
                alert.show()
            } 
            else { // 수정

                // 서버에 실제 요청
                ServerUtil.putRequestTopicReply(mContext, mReplyId, content, object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(json: JSONObject) {
                        val code = json.getInt("code")
                        val message = json.getString("message")

                        runOnUiThread {
                            if (code == 200) {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()

                                // 토픽 상세보기로 이동
                                val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
                                myIntent.putExtra("topic_id",mTopicId)
                                startActivity(myIntent)
                                
                            } else {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            }
        }
    }

    override fun setValues() {

        topicTitleTxt.text = intent.getStringExtra("topicTitle")
        mySideTitleTxt.text = intent.getStringExtra("mySideTitle")
        mTopicId = intent.getIntExtra("topicId", -1)

        // 이미 등록된 의견을 클릭해서 수정하려고 들어올 때 세팅
        mReplyId = intent.getIntExtra("replyId", -1)
        mIsNew = intent.getBooleanExtra("isNew", true)
        contentEdt.setText(intent.getStringExtra("replyContent"))

        // 신규 등록, 수정 여부에 따라 버튼 텍스트 변경
        if (mIsNew) {
            postReplyBtn.text = "의견 등록하기"
        } else {
            postReplyBtn.text = "의견 수정하기"
        }
    }
}