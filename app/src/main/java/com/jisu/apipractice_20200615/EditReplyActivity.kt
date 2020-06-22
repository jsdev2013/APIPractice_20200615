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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_reply)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        
        // 의견 작성 후 버튼 클릭
        // "정말 의견을 등록하시겠습니까? 한번 의견을 등록하면 투표를 변경할 수 없고, 내용을 수정할 수 없습니다."
        // 확인 누르면, 실제로 의견 등록 처리
        // 완료 후 토론 진행 화면 복귀 => 자동 새로 고침
        postReplyBtn.setOnClickListener {

            val content = contentEdt.text.toString()
            if(content.length < 5) {
                Toast.makeText(mContext, "의견을 최소 5자 이상 쓰셔야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val topic_id = intent.getStringExtra("topicId")

            val alert = AlertDialog.Builder(this)
            alert.setTitle("의견 등록 확인")
            alert.setMessage("정말 의견을 등록하시겠습니까? 한번 의견을 등록하면 투표를 변경할 수 없고, 내용을 수정할 수 없습니다.")
            alert.setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->

                ServerUtil.postRequestTopicReplay(mContext,  topic_id.toInt(), content, object: ServerUtil.JsonResponseHandler{
                    override fun onResponse(json: JSONObject) {
                        val code = json.getInt("code")
                        val message = json.getString("message")

                        runOnUiThread {
                            if (code == 200) {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
            })

            alert.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which -> null })
            alert.show()

            return@setOnClickListener
        }

        topicTitleTxt.text = intent.getStringExtra("topicTitle")
        mySideTitleTxt.text = intent.getStringExtra("mySideTitle")
    }
}