package com.jisu.apipractice_20200615

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject

class SignUpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        emailCheckBtn.setOnClickListener {
            // 이메일 받아오기
            val email = emailEdt.text.toString()
            
            // 서버에 중복확인 요청
            ServerUtil.getRequestDuplicatedCheck(mContext, "EMAIL", email, object: ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
                    // 서버의 응답을 처리하는 코드들
                    // 제일 큰 껍데기 json 변수에서 추출
                    val code = json.getInt("code")

                    runOnUiThread {
                        if (code == 200) {
                            emailCheckResultTxt.text = "사용해도 좋습니다."

                        } else {
                            emailCheckResultTxt.text = "중복된 이메일이라 사용 불가합니다."
                        }
                    }
                }

            })
        }

        nickNameCheckBtn.setOnClickListener {
            val nickName = nickNameEdt.text.toString()

            // 서버에 중복확인 요청
            ServerUtil.getRequestDuplicatedCheck(mContext, "NICK_NAME", nickName, object: ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
                    // 서버의 응답을 처리하는 코드들
                    // 제일 큰 껍데기 json 변수에서 추출
                    val code = json.getInt("code")
                    val message = json.getString("message")

                    runOnUiThread {
                        nickNameCheckResultTxt.text = message
//                        if (code == 200) {
//                            nickNameCheckResultTxt.text = message
//
//                        } else {
//                            nickNameCheckResultTxt.text = message
//                        }
                    }
                }

            })
        }
    }

    override fun setValues() {

    }
}
