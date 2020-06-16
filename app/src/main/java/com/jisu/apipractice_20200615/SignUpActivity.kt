package com.jisu.apipractice_20200615

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject

class SignUpActivity : BaseActivity() {

    var isEmailOk = false
    var okEmailTxt = ""
    var isNickNameOk = false
    var okNickNameTxt = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        signUpBtn.setOnClickListener {

            //  서버에 회원 가입 요청
            val inputEmail = emailEdt.text.toString()
            val inputPassword = passwordEdt.text.toString()
            val inputNickName = nickNameEdt.text.toString()

//            회원가입 API를 호출하기 전에 자체 검사
//            각 순서대로 검사 => 틀린게 발견되면 어디서 틀렸는지 토스트로 띄우고 클릭이벤트 종료
//            1) 이메일 중복검사 통과해야 함
                if(!isEmailOk || !inputEmail.equals(okEmailTxt)) {
                    Toast.makeText(mContext, "이메일 중복검사를 통과해야 합니다.", Toast.LENGTH_SHORT).show()

//                    return: 함수의 수행결과를 지정하는 문구
//                    리턴타입 X의 return: 함수를 강제종료시키는 의미로 주고 사용
                    return@setOnClickListener
//            2) 닉네임 중복검사 통과해야 함
                } else if(!isNickNameOk || !inputNickName.equals(okNickNameTxt)){
                    Toast.makeText(mContext, "닉네임 중복검사를 통과해야 합니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
//            3) 비번은 8글자 이상이어야 함
                } else if(passwordEdt.length() < 8) {
                    Toast.makeText(mContext, "비번은 8글자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

            ServerUtil.putRequestSignUp(mContext, inputEmail, inputPassword, inputNickName, object: ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

                    val code = json.getInt("code")

                    runOnUiThread {
                        if (code == 200) {
                            Toast.makeText(mContext, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(mContext, "회원 정보가 없습니다. 확인하고 로그인해주세요.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            })
        }
        
        emailCheckBtn.setOnClickListener {
            // 이메일 받아오기
            val email = emailEdt.text.toString()
            isEmailOk = false

            // 서버에 중복확인 요청
            ServerUtil.getRequestDuplicatedCheck(mContext, "EMAIL", email, object: ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
                    // 서버의 응답을 처리하는 코드들
                    // 제일 큰 껍데기 json 변수에서 추출
                    val code = json.getInt("code")

                    runOnUiThread {
                        if (code == 200) {
                            emailCheckResultTxt.text = "사용해도 좋습니다."
                            okEmailTxt = email
                            isEmailOk = true
                        } else {
                            emailCheckResultTxt.text = "중복된 이메일이라 사용 불가합니다."
                        }
                    }
                }

            })
        }

        nickNameCheckBtn.setOnClickListener {
            val nickName = nickNameEdt.text.toString()
            isNickNameOk = false

            // 서버에 중복확인 요청
            ServerUtil.getRequestDuplicatedCheck(mContext, "NICK_NAME", nickName, object: ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
                    // 서버의 응답을 처리하는 코드들
                    // 제일 큰 껍데기 json 변수에서 추출
                    val code = json.getInt("code")
                    val message = json.getString("message")

                    runOnUiThread {
                        if (code == 200) {
                            nickNameCheckResultTxt.text = message
                            okNickNameTxt = nickName
                            isNickNameOk = true
                        } else {
                            nickNameCheckResultTxt.text = message
                        }
                    }
                }
            })
        }
    }

    override fun setValues() {

    }
}
