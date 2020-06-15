package com.jisu.apipractice_20200615

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jisu.apipractice_20200615.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_sign_up.*

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
            ServerUtil.
        }
    }

    override fun setValues() {

    }
}
