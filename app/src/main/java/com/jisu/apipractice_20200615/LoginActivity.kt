package com.jisu.apipractice_20200615

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.jisu.apipractice_20200615.utils.ContextUtil
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        // 자동로그인 체크박스의 값 변화 이벤트
        autoLoginCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("체크박스 값", isChecked.toString())
            
            // 체크가 됐다면 => ContextUtil 자동 로그인 true로 저장
            // 체크가 해제되었다면 => ContextUtil로 자동 로그인 false로 저장
            ContextUtil.setAutoLogin(mContext, isChecked)
        }

        loginBtn.setOnClickListener {
            val email = emailEdt.text.toString()
            val pw = passwordEdt.text.toString()

            // kj_cho@nepp.kr
            ServerUtil.postRequestLogin(mContext, email, pw, object :ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {
                    // 서버의 응답을 처리하는 코드들
                    // 제일 큰 껍데기 json 변수에서 추출
                    val codeNumber = json.getInt("code")

                    if (codeNumber == 200) {
                        //로그인 성공

                        //성공 시 내려주는 토큰값 추출(token 변수에 저장)
                        val data = json.getJSONObject("data")
                        val token = data.getString("token")

                        //폰에 저장해두는 게 편리함
                        ContextUtil.setUserToken(mContext, token)

                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)

                        finish()
                        
                    } else {
                        // 로그인 실패
                        // UI 반영: 서버가 알려주는 실패 사유를 출력
                        // 인터넷 => 백그라운드 쓰레드 => UI 접근 => 강제종료
                        // UI쓰레드가 => 토스트를 띄우도록
                        val message = json.getString("message")

                        runOnUiThread {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            })
        }

        signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {

    }
}
